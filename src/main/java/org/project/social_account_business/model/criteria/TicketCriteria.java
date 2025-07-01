package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.project.social_account_business.model.Ticket;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TicketCriteria implements Serializable {
    private String title;
    private Integer status;

    public Specification<Ticket> toSpecification() {
        return new Specification<Ticket>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.hasText(title)) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }

                if (status != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                query.orderBy(criteriaBuilder.asc(root.get("title")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
