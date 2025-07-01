package org.project.social_account_business.repository;

import org.project.social_account_business.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    // Custom query methods can be defined here if needed
    // For example, find orders by account ID or status
    // List<Order> findByAccountId(Long accountId);
    // List<Order> findByStatus(String status);
    Optional<Order> findFirstById(Long id);

    @Query("SELECT o FROM Order o INNER JOIN TicketProduct td ON o.ticketProduct.id = td.id WHERE td.itemCode = :itemCode")
    Optional<Order> findAllByTicketProductItemCode(@Param("itemCode") String itemCode);

    @Query("SELECT COALESCE(SUM(o.quantity), 0) FROM Order o WHERE o.account.id = :accountId AND o.ticketProduct.id = :ticketProductId")
    int getTotalPurchasedQuantity(@Param("accountId") Long accountId, @Param("ticketProductId") Long ticketProductId);
}
