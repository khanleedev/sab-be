package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX + "tickets", indexes = {
        @Index(name = "idx_ticket_name", columnList = "name"),
})
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Ticket extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    @Column(name = "name", nullable = false)
    private String title;
    @Lob
    private byte[] image;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TicketProduct> ticketProducts = new ArrayList<>();
}
