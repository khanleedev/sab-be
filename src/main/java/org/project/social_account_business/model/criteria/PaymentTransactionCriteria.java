package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.*;
import lombok.Data;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.PaymentTransaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaymentTransactionCriteria implements Serializable {
    private String sepayTransactionId;
    private Long accountId;
    private String accountEmail;
    private String accountNumber;

    public Specification<PaymentTransaction> getSpecifition() {
        return new Specification<PaymentTransaction>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PaymentTransaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.hasText(sepayTransactionId)) {
                    predicates.add(criteriaBuilder.equal(root.get("sepayTransactionId"), sepayTransactionId));
                }
                if (accountId != null) {
                    Join<PaymentTransaction, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(accountJoin.get("id"), accountId));
                }
                if (StringUtils.hasText(accountEmail)) {
                    Join<PaymentTransaction, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(accountJoin.get("email"), accountEmail));
                }
                if (StringUtils.hasText(accountNumber)) {
                    Join<PaymentTransaction, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(accountJoin.get("accountNumber"), accountNumber));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
