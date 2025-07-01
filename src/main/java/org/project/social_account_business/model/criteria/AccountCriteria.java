package org.project.social_account_business.model.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.project.social_account_business.model.Account;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AccountCriteria implements Serializable {
    private Long id;
    private int kind;
    private String username;
    private Integer status;
    private String email;
    private String phone;

    public Specification<Account> getSpecification() {
        return new Specification<Account>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.hasText(username)) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getKind() > 0) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (StringUtils.hasText(username)) {
                    predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
                }
                if (StringUtils.hasText(email)) {
                    predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
                }
                if (StringUtils.hasText(phone)) {
                    predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}