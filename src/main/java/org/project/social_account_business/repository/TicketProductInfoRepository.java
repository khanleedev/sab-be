package org.project.social_account_business.repository;

import org.project.social_account_business.model.TicketProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketProductInfoRepository extends JpaRepository<TicketProductInfo, Long> , JpaSpecificationExecutor<TicketProductInfo> {
}
