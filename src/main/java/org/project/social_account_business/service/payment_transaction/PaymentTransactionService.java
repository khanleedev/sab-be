package org.project.social_account_business.service.payment_transaction;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.payment_transaction.PaymentTransactionDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;
import org.project.social_account_business.form.payment_transaction.CreatePaymentTransactionForm;
import org.project.social_account_business.form.payment_transaction.PaymentTransactionWebhookForm;
import org.project.social_account_business.model.PaymentTransaction;
import org.project.social_account_business.model.criteria.PaymentTransactionCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentTransactionService {
    /**
     * Create a new payment transaction for sepay.
     *
     * @param createPaymentTransactionForm the form containing transaction details
     * @return the created transaction
     */
    void createPaymentTransactionForSepay(CreatePaymentTransactionForm createPaymentTransactionForm);

    /**
     * Get a payment transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return the transaction with the specified ID
     */
    PaymentTransactionDto getPaymentTransactionById(Long id);

    /**
     * Get all payment transactions with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param criteria the criteria for filtering transactions
     * @return a page of transactions matching the criteria
     */
    ResponseListDto<List<PaymentTransactionDto>> getAllTransactions(Pageable pageable, PaymentTransactionCriteria criteria);

    /**
     * Get payment transaction details by ID.
     *
     * @param id the ID of the transaction to obtain
     */
    PaymentTransactionDto getPaymentTransactionDetailsById(Long id);

    /**
     * Handle a webhook response from Sepay.
     *
     * @param sepayWebhookResponseForm the form containing the webhook response
     * @return the created payment transaction
     */
    CreatePaymentTransactionForm handleSepayPaymentWebhook(PaymentTransactionWebhookForm sepayWebhookResponseForm);

    /**
     * Get a shortened payment transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return the shortened transaction with the specified ID
     */
    ShortenPaymentTransactionDto getShortenPaymentTransactionById(Long id);

    /**
     * Save a payment transaction and return its ID.
     *
     * @param paymentTransaction the transaction to save
     * @return the ID of the saved transaction
     */
    Long saveAndGetId(PaymentTransaction paymentTransaction);
}
