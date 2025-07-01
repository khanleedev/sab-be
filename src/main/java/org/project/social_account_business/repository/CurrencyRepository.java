package org.project.social_account_business.repository;

import org.project.social_account_business.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findFirstByCode(String code);

    @Query("SELECT c from Currency c WHERE c.status = 1")
    Page<Currency> findAllActiveCurrency(Pageable pageable);
}
