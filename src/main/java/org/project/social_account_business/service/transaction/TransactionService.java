package org.project.social_account_business.service.transaction;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;
import org.project.social_account_business.dto.transaction.TransactionForPaymentDto;
import org.project.social_account_business.form.payment_transaction.CreateTopUpForm;
import org.project.social_account_business.model.Order;
import org.project.social_account_business.model.Transaction;
import org.project.social_account_business.model.criteria.TransactionCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    /**
     * Create a new transaction for order.
     *
     * @param order the order for which the transaction is created
     * @return the created transaction
     */
    String createTransactionForOrder(Order order);

    /**
     * Create a new payment transaction.
     *
     * @param createTopUpForm the form containing transaction details
     */
    String createTransactionForSepayPayment(CreateTopUpForm createTopUpForm);

    /**
     * Get a transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return the transaction with the specified ID
     */
    TransactionDto getTransactionById(Long id);

    /**
     * Get all transactions for order with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param criteria the criteria for filtering transactions
     * @return a page of transactions for order matching the criteria
     */
    ResponseListDto<List<TransactionForOrderDto>> getTransactionsForOrder(Pageable pageable, TransactionCriteria criteria);

    /**
     * Get all transactions for payment with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param criteria the criteria for filtering transactions
     * @return a page of transactions for payment matching the criteria
     */
    ResponseListDto<List<TransactionForPaymentDto>> getTransactionsForPayment(Pageable pageable, TransactionCriteria criteria);

    /**
     * Get all transactions with pagination and filtering.
     *
     * @param pageable the pagination information
     * @param criteria the criteria for filtering transactions
     * @return a page of transactions matching the criteria
     */
    ResponseListDto<List<TransactionDto>> getAllTransactions(Pageable pageable, TransactionCriteria criteria);

    /**
     * Get transaction details by ID.
     *
     * @param id the ID of the transaction to obtain
     */
    TransactionDto getTransactionDetailsById(Long id);

    /**
     * Save transaction to database.
     *
     * @param transaction the transaction to save
     */
    void saveTransaction(Transaction transaction);

    /**
     * Find transaction by ID.
     *
     * @param id the ID of the transaction to find
     * @return the transaction with the specified ID
     */
    Transaction findTransactionById(Long id);

    /**
     * Find transaction by transaction code.
     *
     * @param transactionCode the transaction code to find
     * @return the transaction with the specified transaction code
     */
    Transaction findTransactionByTransactionCode(String transactionCode);

    /**
     * Find transaction by transaction ID.
     *
     * @param transactionId the transaction ID to find
     * @return the transaction with the specified transaction ID
     */
    Transaction findTransactionByTransactionId(Long transactionId);

    /**
     * Get all transactions for order by account ID with pagination.
     *
     * @param pageable the pagination information
     * @param accountId the account ID to filter transactions
     * @return a page of transactions for order matching the account ID
     */
    ResponseListDto<List<TransactionForOrderDto>> getAllTransactionsForOrderByAccountId(Pageable pageable, long accountId);

    /**
     * Get all transactions for payment by account ID with pagination.
     *
     * @param pageable the pagination information
     * @param accountId the account ID to filter transactions
     * @return a page of transactions for payment matching the account ID
     */
    ResponseListDto<List<TransactionForPaymentDto>> getAllTransactionsForPaymentByAccountId(Pageable pageable, long accountId);
}
