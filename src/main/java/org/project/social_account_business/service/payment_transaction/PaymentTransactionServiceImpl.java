package org.project.social_account_business.service.payment_transaction;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ApiMessageDto;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.payment_transaction.PaymentTransactionDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.payment_transaction.CreatePaymentTransactionForm;
import org.project.social_account_business.form.payment_transaction.PaymentTransactionWebhookForm;
import org.project.social_account_business.mapper.PaymentTransactionMapper;
import org.project.social_account_business.model.PaymentTransaction;
import org.project.social_account_business.model.TransactionStatus;
import org.project.social_account_business.model.criteria.PaymentTransactionCriteria;
import org.project.social_account_business.repository.PaymentTransactionRepository;
import org.project.social_account_business.service.account.AccountService;
import org.project.social_account_business.service.email.EmailService;
import org.project.social_account_business.service.transaction.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("paymentTransactionService")
@Slf4j
public class PaymentTransactionServiceImpl implements PaymentTransactionService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository, PaymentTransactionMapper paymentTransactionMapper, AccountService accountService, TransactionService transactionService, EmailService emailService, SimpMessagingTemplate messagingTemplate) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.emailService = emailService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPaymentTransactionForSepay(CreatePaymentTransactionForm createPaymentTransactionForm) {
        val paymentTransaction = paymentTransactionMapper.fromCreatePaymentTransactionFormToEntity(createPaymentTransactionForm);
        String transactionCode = "";
        try {
            String desValue = createPaymentTransactionForm.getDescription();
            if (desValue.contains("SAB TOP UP FOR ACCOUNT")) {
                Pattern pattern = Pattern.compile("PAYMENT\\d+");
                Matcher matcher = pattern.matcher(desValue);
                if (matcher.find()) {
                    transactionCode = matcher.group();
                    log.info("Transaction code is {}", transactionCode);
                } else {
                    throw new BadRequestException("[PaymentTransactionService] No transaction code found in description");
                }
            }
        } catch (Exception e) {
            log.error("[PaymentTransactionService] Error parsing transaction code from description: {}", e.getMessage());
            throw new BadRequestException("[PaymentTransactionService] Invalid transaction code in description");
        }
        val transaction = transactionService.findTransactionByTransactionCode(transactionCode);
        val account = accountService.findAccountByEmail(transaction.getCreatedBy());
        if (transaction.getAmountInCash().doubleValue() != createPaymentTransactionForm.getAmountIn().doubleValue()) {
            throw new BadRequestException("[PaymentTransactionService] Amount in cash does not match the payment transaction amount");
        }

        account.setBalance(account.getBalance() + transaction.getAmountInCoin());

        paymentTransaction.setAccount(account);
        Long id = paymentTransactionRepository.save(paymentTransaction).getId();
        transaction.setOrderStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionId(id);
        transactionService.saveTransaction(transaction);

        log.info("Sending payment completion message to user {}", account.getId());
        messagingTemplate.convertAndSendToUser(
                account.getEmail(),
                "/queue/payment-status",
                new ApiMessageDto<>("COMPLETE", "Payment transaction created successfully")
        );
        log.info("Payment completion message sent to user {}", account.getId());
        emailService.sendEmail(account.getEmail(), "Payment Confirmation", "Your payment of " + createPaymentTransactionForm.getAmountIn() + " has been received successfully.", false);

    }

    @Override
    @Transactional(readOnly = true)
    public PaymentTransactionDto getPaymentTransactionById(Long id) {
        return paymentTransactionRepository.findById(id)
                .map(paymentTransactionMapper::fromEntityToDto)
                .orElseThrow(() -> new NotFoundException("[PaymentTransactionService] Payment transaction not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<PaymentTransactionDto>> getAllTransactions(Pageable pageable, PaymentTransactionCriteria criteria) {
        Page<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findAll(criteria.getSpecifition(), pageable);
        return new ResponseListDto<>(paymentTransactionMapper.fromEntitiesToDtos(paymentTransactions.getContent()),
                paymentTransactions.getTotalElements(),
                paymentTransactions.getTotalPages());
    }

    @Override
    public PaymentTransactionDto getPaymentTransactionDetailsById(Long id) {
        return paymentTransactionRepository.findFirstById(id)
                .map(paymentTransactionMapper::fromEntityToDto)
                .orElseThrow(() -> new NotFoundException("[PaymentTransactionService] Payment transaction not found with id: " + id));
    }

    @Override
    public CreatePaymentTransactionForm handleSepayPaymentWebhook(PaymentTransactionWebhookForm sepayWebhookResponseForm) {
        log.info("[PaymentTransactionService] Received Sepay payment webhook: {}", sepayWebhookResponseForm);
        CreatePaymentTransactionForm createPaymentTransactionForm = new CreatePaymentTransactionForm();
        createPaymentTransactionForm.setSepayTransactionId(sepayWebhookResponseForm.getId());
        createPaymentTransactionForm.setGateway(sepayWebhookResponseForm.getGateway());
        createPaymentTransactionForm.setAccountNumber(sepayWebhookResponseForm.getAccountNumber());
        createPaymentTransactionForm.setTransactionDate(sepayWebhookResponseForm.getTransactionDate());
        createPaymentTransactionForm.setCode(sepayWebhookResponseForm.getCode());
        createPaymentTransactionForm.setDescription(sepayWebhookResponseForm.getContent());
        createPaymentTransactionForm.setAmountIn(BigDecimal.valueOf(sepayWebhookResponseForm.getTransferAmount()));
        createPaymentTransactionForm.setReferenceNumber(sepayWebhookResponseForm.getReferenceCode());
        createPaymentTransactionForm.setAccumulated(BigDecimal.valueOf(sepayWebhookResponseForm.getAccumulated()));
        createPaymentTransactionForm.setCallbackUrl("103.255.238.9/v1/payment-transactions/webhooks/sepay-payment");
        createPaymentTransactionForm.setCurrencyCode(BetaConstant.SEPAY_METHOD_CURRENCY_CODE);
        return createPaymentTransactionForm;
    }

    @Override
    public ShortenPaymentTransactionDto getShortenPaymentTransactionById(Long id) {
        return paymentTransactionMapper.fromEntityToShortenDto(paymentTransactionRepository.findFirstById(id).orElseThrow(
                () -> new NotFoundException("[PaymentTransactionService] Payment transaction not found with id: " + id, ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND)
        ));
    }

    @Override
    public Long saveAndGetId(PaymentTransaction paymentTransaction) {
        return paymentTransactionRepository.save(paymentTransaction).getId();
    }
}
