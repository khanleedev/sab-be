package org.project.social_account_business;

import org.junit.jupiter.api.*;
import org.project.social_account_business.dto.ApiMessageDto;
import org.project.social_account_business.form.report.CreateReportForm;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.service.auth.TokenService;
import org.project.social_account_business.service.payment_transaction.PaymentTransactionService;
import org.project.social_account_business.service.report.ReportService;
import org.project.social_account_business.service.report.ReportServiceImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SocialAccountBusinessApplicationTests {
    @LocalServerPort
    private int port;

    private static WebSocketStompClient stompClient;
    private StompSession stompSession;
    private ReportService reportService;
    private TokenService tokenService;

    SocialAccountBusinessApplicationTests(ReportServiceImpl reportService, TokenService tokenService) {
        this.reportService = reportService;
        this.tokenService = tokenService;
    }

    private final String email = "testuser@example.com";
    private final String jwtToken = tokenService.generateTokenPair(new Account((long)2002,1,"test",email)).getAccessToken();
    private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

    @Test
    void contextLoads() {
    }

    @BeforeAll
    static void setup() {
        stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @BeforeEach
    void connect() throws Exception {
        String url = "ws://localhost:" + port + "/ws";

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + jwtToken);

        stompSession = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {
        }, connectHeaders).get(3, TimeUnit.SECONDS);

        StompHeaders subscribeHeaders = new StompHeaders();
        subscribeHeaders.setDestination("/account/queue/report");

        stompSession.subscribe(subscribeHeaders, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ApiMessageDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ApiMessageDto<?> message = (ApiMessageDto<?>) payload;
                blockingQueue.offer(message.getMessage());
            }
        });
    }

    @Test
    void shouldReceivePaymentMessageWhenPaymentIsCompleted() throws Exception {
        String message = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertThat(message).isEqualTo("Report created successfully");
    }

    @AfterEach
    void cleanup() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
        }
    }
}
