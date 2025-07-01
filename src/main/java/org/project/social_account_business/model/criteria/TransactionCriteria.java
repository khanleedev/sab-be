package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.*;
import lombok.Data;
import org.project.social_account_business.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionCriteria implements Serializable {
    private String transactionType;
    private String orderStatus;

    public Specification<Transaction> getSpecification() {
        return new Specification<Transaction>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.hasText(transactionType)) {
                    predicates.add(criteriaBuilder.equal(root.get("transactionType"), transactionType));
                }
                if(StringUtils.hasText(orderStatus)) {
                    predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
