package org.project.social_account_business.model;

import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX + "ticket_products", indexes = {
        @Index(name = "idx_ticket_product_name", columnList = "name"),
        @Index(name = "idx_ticket_product_item_code", columnList = "item_code"),
        @Index(name = "idx_ticket_product_ticket_id_item_code", columnList = "ticket_id, item_code"),
})
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class TicketProduct extends Auditable<String> {
    @Id
    @SystemId
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price")
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    @Column(name = "description")
    private String description;
    @Column(name = "item_code")
    private String itemCode;
    @Column(name = "max_purchase_per_account")
    private Integer maxPurchasePerAccount = 1;
    @OneToMany(mappedBy = "ticketProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "ticketProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketProductInfo> ticketProductInfos = new ArrayList<>();
}
