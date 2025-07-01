package org.project.social_account_business.repository;

import org.project.social_account_business.model.TokenInfo;
import org.project.social_account_business.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenInfo, Long> {
    TokenInfo findByToken(String token);
    boolean existsByToken(String token);
    void deleteByToken(String token);
    @Modifying
    @Query("DELETE FROM TokenInfo t WHERE t.account.email = :email and t.type = :tokenType")
    void deleteAllByAccountEmailAndType(String email, TokenType tokenType);
}
