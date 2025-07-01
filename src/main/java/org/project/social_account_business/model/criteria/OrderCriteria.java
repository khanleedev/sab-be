package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.*;
import lombok.Data;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCriteria implements Serializable {
    private Long ticketProductId;
    private Long accountId;

    public Specification<Order> getSpecification() {
        return new Specification<Order>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ticketProductId != null) {
                    Join<Order, TicketProduct> ticketProductJoin = root.join("ticketProduct", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(ticketProductJoin.get("id"), ticketProductId));
                }
                if (accountId != null) {
                    Join<Order, Account> accountJoin = root.join("account", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(accountJoin.get("id"), accountId));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
