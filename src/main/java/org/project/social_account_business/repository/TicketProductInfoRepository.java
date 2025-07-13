package org.project.social_account_business.repository;

import org.project.social_account_business.model.TicketProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketProductInfoRepository extends JpaRepository<TicketProductInfo, Long> , JpaSpecificationExecutor<TicketProductInfo> {
    Page<TicketProductInfo> findAllByTicketProductId(Long ticketProductId, Pageable pageable);

    @Query("SELECT t FROM TicketProductInfo t WHERE t.ticketProduct.id = :ticketProductId AND t.isSold = false order by t.id ASC LIMIT :limit")
    List<TicketProductInfo> findAvailableRandomInfos(@Param("ticketProductId") Long ticketProductId,
                                                     @Param("limit") int limit);


}
