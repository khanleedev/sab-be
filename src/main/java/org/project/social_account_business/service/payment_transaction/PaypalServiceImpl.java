package org.project.social_account_business.service.payment_transaction;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.coyote.BadRequestException;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.dto.payment_transaction.PaypalCaptureDto;
import org.project.social_account_business.dto.payment_transaction.PaypalResponseDto;
import org.project.social_account_business.form.payment_transaction.PaypalRequestForm;
import org.project.social_account_business.model.PaymentTransaction;
import org.project.social_account_business.model.Transaction;
import org.project.social_account_business.model.TransactionStatus;
import org.project.social_account_business.model.TransactionType;
import org.project.social_account_business.service.account.AccountService;
import org.project.social_account_business.service.currency.CurrencyService;
import org.project.social_account_business.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@Service("paypalService")
@Slf4j
public class PaypalServiceImpl implements PaypalService {
    private static final String PAYPAL_TRANSACTION_PREFIX = "PAYPAL";
    private PayPalHttpClient payPalHttpClient;
    private final PaymentTransactionService paymentTransactionService;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @Value("${paypal.webhook.id}")
    private String webhookId;

    public PaypalServiceImpl(PaymentTransactionService paymentTransactionService, TransactionService transactionService, AccountService accountService, CurrencyService currencyService) {
        this.paymentTransactionService = paymentTransactionService;
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public PaypalResponseDto createPaypalOrder(PaypalRequestForm paypalRequestForm, long accountId) throws IOException {
        try {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");

            ApplicationContext appContext = new ApplicationContext()
                    .brandName("SAB")
                    .landingPage("BILLING")
                    .userAction("PAY_NOW")
                    .returnUrl(successUrl)
                    .cancelUrl(cancelUrl);
            orderRequest.applicationContext(appContext);

            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(new PurchaseUnitRequest()
                    .amountWithBreakdown(new AmountWithBreakdown()
                            .currencyCode(paypalRequestForm.getCurrency())
                            .value(String.format("%.2f", paypalRequestForm.getAmount()))
                            .amountBreakdown(new AmountBreakdown()
                                    .itemTotal(new Money()
                                            .currencyCode(paypalRequestForm.getCurrency())
                                            .value(String.format("%.2f", paypalRequestForm.getAmount())))))
                    .description(paypalRequestForm.getDescription()));
            orderRequest.purchaseUnits(purchaseUnits);

            OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            PaypalResponseDto responseDto = new PaypalResponseDto();
            responseDto.setOrderId(order.id());
            responseDto.setStatus(order.status());
            for (LinkDescription link : order.links()) {
                if ("approve".equals(link.rel())) {
                    responseDto.setApprovalLink(link.href());
                }
            }

            val account = accountService.findById(accountId);

            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setSepayTransactionId(order.id());
            paymentTransaction.setGateway("PAYPAL");
            paymentTransaction.setCallbackUrl(responseDto.getApprovalLink());
            paymentTransaction.setDescription(paypalRequestForm.getDescription());
            paymentTransaction.setAmountIn(BigDecimal.valueOf(paypalRequestForm.getAmount()));
            paymentTransaction.setAccount(account);
            val transactionId = paymentTransactionService.saveAndGetId(paymentTransaction);

            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionId);
            transaction.setCreatedBy(account.getEmail());
            transaction.setCreatedDate(new Date());
            transaction.setTransactionType(TransactionType.IN);
            transaction.setTransactionCode(order.id());
            transaction.setAmountInCash(BigDecimal.valueOf(paypalRequestForm.getAmount()));
            transaction.setAmountInCoin(paypalRequestForm.getAmount() * currencyService.getRateConverterByCode(BetaConstant.VISA_METHOD_CURRENCY_CODE));
            transaction.setOrderStatus(TransactionStatus.PENDING);
            transactionService.saveTransaction(transaction);

            return responseDto;
        } catch (HttpException e) {
            throw new IOException("[PaypalService] Error creating PayPal order: " + e.getMessage(), e);
        }
    }

    @Override
    public PaypalCaptureDto capturePaypalOrder(String orderId) throws IOException {
        try {
            val transaction = transactionService.findTransactionByTransactionCode(orderId);
            if (TransactionStatus.COMPLETED.equals(transaction.getTransactionType())) {
                throw new BadRequestException("Order already captured! Order id: " + orderId);
            }
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();
            transaction.setOrderStatus(TransactionStatus.COMPLETED);
            transactionService.saveTransaction(transaction);

            val account = accountService.findAccountByEmail(transaction.getCreatedBy());

            PaypalCaptureDto captureDto = new PaypalCaptureDto();
            captureDto.setOrderId(order.id());
            captureDto.setStatus(order.status());
            captureDto.setUserId(account.getId().toString());
            if (!order.purchaseUnits().isEmpty()) {
                captureDto.setAmount(order.purchaseUnits().get(0).amountWithBreakdown().value());
                captureDto.setCurrency(order.purchaseUnits().get(0).amountWithBreakdown().currencyCode());
            }
            return captureDto;
        } catch (Exception e) {
            throw new IOException("[PaypalService] Error capturing PayPal order: " + e.getMessage(), e);
        }
    }

    @Override
    public void handlePaypalWebhook(String payload, String authAlgo, String certUrl, String transmissionId, String transmissionSig, String transmissionTime) throws IOException {

    }
}
