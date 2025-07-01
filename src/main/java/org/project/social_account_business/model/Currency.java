package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Setter
@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TablePrefix.PREFIX + "currencies", indexes = {
        @Index(name = "idx_currency_code", columnList = "code"),
        @Index(name = "idx_currency_name", columnList = "name")
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EntityListeners(AuditingEntityListener.class)
public class Currency extends Auditable<String>{
    @Id
    @SystemId
    private Long id;
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "bonus_rate")
    private Double bonusRate;
}
