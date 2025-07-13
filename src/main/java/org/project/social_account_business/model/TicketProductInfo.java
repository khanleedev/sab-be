package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = TablePrefix.PREFIX + "ticket_product_infos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class TicketProductInfo extends Auditable<String>{

    @Id
    @SystemId
    private Long id;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "pass", nullable = false)
    private String pass;

    @Column(name = "2_fa")
    private String twoFA;

    @Column(name = "mail")
    private String mail;

    @Column(name = "pass_mail")
    private String passMail;

    @Column(name = "mail_verify")
    private String mailVerify;

    @Column(name = "isSold", nullable = false)
    private Boolean isSold = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_product_id", nullable = false)
    private TicketProduct ticketProduct;
}
