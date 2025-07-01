package org.project.social_account_business.repository;

import jakarta.persistence.LockModeType;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    /**
     * Get transaction by id
     * @param id
     * @return
     */
    Optional<Transaction> findFirstById(Long id);

    /**
     * Get transaction by transaction code
     * @param transactionCode
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transaction t WHERE t.transactionCode = :transactionCode")
    Optional<Transaction> findFirstByTransactionCode(String transactionCode);

    /**
     * Get transaction by transaction code and order status
     * @param transactionType
     * @param orderStatus
     * @return
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :transactionType AND t.orderStatus = :orderStatus")
    Optional<Transaction> findFirstByTransactionTypeAndOrderStatus(@Param("transactionType") String transactionType, @Param("orderStatus") String orderStatus);

    @Query("SELECT t FROM Transaction t WHERE t.transactionId = :transactionId")
    Optional<Transaction> findTransactionByTransactionId(@Param("transactionId") long transactionId);

    @Query("SELECT t FROM Transaction t INNER JOIN Order o ON t.transactionId = o.id WHERE o.account.id = :accountId")
    Page<Transaction> findAllTransactionForOrderByAccountId(@Param("accountId") long accountId, Pageable pageable);

    @Query("SELECT t FROM Transaction t INNER JOIN PaymentTransaction p ON t.transactionId = p.id WHERE p.account.id = :accountId")
    Page<Transaction> findAllTransactionForPaymentByAccountId(@Param("accountId") long accountId, Pageable pageable);
}
