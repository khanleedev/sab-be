package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = TablePrefix.PREFIX + "reports",
        indexes = {
                @Index(name = "idx_report_status", columnList = "status")
        })

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Report extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Column(name = "report_content", nullable = false)
    private String reportContent;
}
