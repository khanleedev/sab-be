package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;
import org.project.social_account_business.dto.transaction.TransactionForPaymentDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.payment_transaction.CreateTopUpForm;
import org.project.social_account_business.model.TransactionType;
import org.project.social_account_business.model.criteria.TransactionCriteria;
import org.project.social_account_business.service.transaction.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/transactions")
@Slf4j
public class TransactionController {
    final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> createTransactionForPayment(@Valid @RequestBody CreateTopUpForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new MyBindingException("[TransactionController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.CREATED, "Transaction created successfully", transactionService.createTransactionForSepayPayment(form)));
    }

    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TransactionForPaymentDto>>>> getTransactionsForSepayPayment(TransactionCriteria transactionCriteria, Pageable pageable) {
        transactionCriteria.setTransactionType(TransactionType.IN.toString());
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Transaction retrieved successfully", transactionService.getTransactionsForPayment(pageable, transactionCriteria)));
    }

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TransactionForOrderDto>>>> getTransactionsForOrder(TransactionCriteria transactionCriteria, Pageable pageable) {
        transactionCriteria.setTransactionType(TransactionType.OUT.toString());
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Transaction retrieved successfully", transactionService.getTransactionsForOrder(pageable, transactionCriteria)));
    }

    @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<EntityModel<TransactionDto>>> getTransactionDetail(@PathVariable Long transactionId) {
        TransactionDto transactionDto = transactionService.getTransactionById(transactionId);
        EntityModel<TransactionDto> entityModel = EntityModel.of(transactionDto,
                linkTo(methodOn(TransactionController.class).getTransactionDetail(transactionId)).withSelfRel(),
                linkTo(methodOn(TransactionController.class).getTransactionsForSepayPayment(new TransactionCriteria(), Pageable.unpaged())).withRel("sepay-transactions"),
                linkTo(methodOn(TransactionController.class).getTransactionsForOrder(new TransactionCriteria(), Pageable.unpaged())).withRel("order-transactions"),
                linkTo(methodOn(TransactionController.class).getAllTransactionsForOrderByAccountId(null, Pageable.unpaged())).withRel("order-transactions-by-account"),
                linkTo(methodOn(TransactionController.class).getAllTransactionForPaymentByAccountId(null, Pageable.unpaged())).withRel("payment-transactions-by-account"));
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Transaction retrieved successfully", entityModel));
    }

    @GetMapping(value = "/order/account", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<TransactionForOrderDto>>>> getAllTransactionsForOrderByAccountId(@Param("accountId") Long accountId, Pageable pageable) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Transaction retrieved successfully", transactionService.getAllTransactionsForOrderByAccountId(pageable, accountId)));
    }

    @GetMapping(value = "/payment/account", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<TransactionForPaymentDto>>>> getAllTransactionForPaymentByAccountId(@Param("accountId") Long accountId, Pageable pageable) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Transaction retrieved successfully", transactionService.getAllTransactionsForPaymentByAccountId(pageable, accountId)));
    }
}
