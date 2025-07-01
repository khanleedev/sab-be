package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.*;
import lombok.Data;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.TicketProduct;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TicketProductCriteria implements Serializable {
    private String name;
    private Double price;
    private Integer status;
    private String itemCode;
    private Double minPrice;
    private Double maxPrice;

    public Specification<TicketProduct> getSpecification() {
        return new Specification<TicketProduct>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<TicketProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.hasText(name)) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }

                if (price != null) {
                    predicates.add(criteriaBuilder.equal(root.get("price"), price));
                }

                if (status != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                if (StringUtils.hasText(itemCode)) {
                    predicates.add(criteriaBuilder.like(root.get("itemCode"), itemCode));
                }

                if (minPrice != null && maxPrice != null) {
                    predicates.add(criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
                } else if (minPrice != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
                } else if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
                }
                query.orderBy(criteriaBuilder.asc(root.get("price")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
