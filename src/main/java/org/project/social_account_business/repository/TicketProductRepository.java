package org.project.social_account_business.repository;

import jakarta.persistence.LockModeType;
import org.project.social_account_business.model.TicketProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface TicketProductRepository extends JpaRepository<TicketProduct, Long>, JpaSpecificationExecutor<TicketProduct> {
    /**
     * Get ticket product by name
     *
     * @param name
     * @return
     */
    TicketProduct findFirstByName(String name);
    /**
     * Check if ticket product exists by name
     *
     * @param name
     * @return
     */
    boolean existsByName(String name);
    /**
     * Check if ticket product exists by id
     *
     * @param id
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TicketProduct> findFirstById(Long id);

    /**
     * Get all ticket products by ticket id
     * @param ticketId
     * @param pageable
     * @return
     */
    @Query("SELECT tp  FROM TicketProduct tp WHERE tp.ticket.id = :ticketId and tp.status = 1 AND tp.quantity > 0")
    Page<TicketProduct> findAllByTicketId(@RequestParam("ticketId") Long ticketId, Pageable pageable);
}
