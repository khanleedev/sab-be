package org.project.social_account_business.repository;

import org.project.social_account_business.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findFirstById(long id);

    @Query("SELECT r FROM Report r WHERE r.account.id = :accountId")
    Page<Report> findAllByAccountId(long accountId, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.status = :status")
    Page<Report> findAllByStatus(int status, Pageable pageable);
}
