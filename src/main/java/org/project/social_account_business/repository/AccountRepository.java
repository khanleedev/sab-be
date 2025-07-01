package org.project.social_account_business.repository;

import org.project.social_account_business.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    @Query("SELECT a FROM Account a WHERE a.username = :username")
    Optional<Account> findAccountByUsername(String username);

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findAccountById(Long id);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.email = :email")
    boolean existsAccountByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.phoneNo = :phoneNo")
    boolean existsAccountByPhoneNo(String phoneNo);

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Optional<Account> findAccountByEmail(String email);
}
