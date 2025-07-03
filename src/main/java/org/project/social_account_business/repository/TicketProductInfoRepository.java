package org.project.social_account_business.repository;

import org.project.social_account_business.model.TicketProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketProductInfoRepository extends JpaRepository<TicketProductInfo, Long> , JpaSpecificationExecutor<TicketProductInfo> {
    List<TicketProductInfo> findAllByTicketProductId(Long ticketProductId);

    @Query(value = """
    SELECT * FROM sab_db_ticket_product_infos 
    WHERE ticket_product_id = :ticketProductId AND is_sold = false 
    ORDER BY RANDOM() 
    LIMIT :limit
""", nativeQuery = true)
    List<TicketProductInfo> findAvailableRandomInfos(@Param("ticketProductId") Long ticketProductId,
                                                     @Param("limit") int limit);


}
