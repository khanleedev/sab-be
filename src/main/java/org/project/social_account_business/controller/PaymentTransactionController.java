package org.project.social_account_business.controller;

import lombok.extern.slf4j.Slf4j;
import com.paypal.orders.*;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.payment_transaction.PaymentTransactionDto;
import org.project.social_account_business.dto.payment_transaction.PaypalCaptureDto;
import org.project.social_account_business.dto.payment_transaction.PaypalResponseDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.payment_transaction.CreatePaymentTransactionForm;
import org.project.social_account_business.form.payment_transaction.PaymentTransactionWebhookForm;
import org.project.social_account_business.form.payment_transaction.PaypalRequestForm;
import org.project.social_account_business.model.criteria.PaymentTransactionCriteria;
import org.project.social_account_business.service.payment_transaction.PaymentTransactionService;
import org.project.social_account_business.service.payment_transaction.PaypalService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/payment-transactions")
@Slf4j
public class PaymentTransactionController {
    private final PaymentTransactionService paymentTransactionService;
    private final PaypalService paypalService;

    public PaymentTransactionController(PaymentTransactionService paymentTransactionService, PaypalService paypalService) {
        this.paymentTransactionService = paymentTransactionService;
        this.paypalService = paypalService;
    }

    @PostMapping(value = "/webhooks/sepay-payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> handleSepayPaymentWebhook(@RequestBody PaymentTransactionWebhookForm sepayWebhookResponseForm, BindingResult bindingResult) {
        log.info("Received Sepay payment webhook: {}", sepayWebhookResponseForm);
        if (bindingResult.hasErrors()) {
            throw new MyBindingException("[PaymentTransactionController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        CreatePaymentTransactionForm createPaymentTransactionForm = paymentTransactionService.handleSepayPaymentWebhook(sepayWebhookResponseForm);
        paymentTransactionService.createPaymentTransactionForSepay(createPaymentTransactionForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Success", "Webhook processed successfully"));
    }



    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> getPaymentTransactionsByAccountId(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get list payment transaction successfully!", paymentTransactionService.getPaymentTransactionDetailsById(id)));
    }

    @GetMapping()
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<PaymentTransactionDto>>>> getAllPaymentTransactions(PaymentTransactionCriteria paymentTransactionCriteria, Pageable pageable) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get list payment transaction successfully!", paymentTransactionService.getAllTransactions(pageable, paymentTransactionCriteria)));
    }

    @PostMapping("/paypal/pay")
    public ResponseEntity<ApiResponse<?>> createPayment(@RequestBody PaypalRequestForm requestDto) {
        try {
            PaypalResponseDto responseDto = paypalService.createPaypalOrder(requestDto, requestDto.getUserId());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Success", responseDto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/paypal/pay/success")
    public ResponseEntity<ApiResponse<?>> successPay(@RequestParam("token") String orderId) {
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            PaypalCaptureDto captureDto = paypalService.capturePaypalOrder(orderId);
            if ("COMPLETED".equals(captureDto.getStatus())) {
                return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Payment transaction completed successfully", captureDto));
            } else {
                return ResponseEntity.status(400).body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Payment transaction could not be completed"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/paypal/capture-order")
    public ResponseEntity<?> captureOrder(@RequestParam String orderId) throws IOException {
        PaypalCaptureDto order = paypalService.capturePaypalOrder(orderId);
        return ResponseEntity.ok(order);
    }
}
