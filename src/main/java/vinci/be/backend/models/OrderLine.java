package vinci.be.backend.models;

import jakarta.persistence.*;
import lombok.*;
import vinci.be.backend.repositories.OrderLineIdentifier;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders_lines")
@Table(name="orders_lines")
@IdClass(OrderLineIdentifier.class)
public class OrderLine {


    @Column(name="planned_quantity", nullable=false)
    private int plannedQuantity;

    @Column(name="delivered_quantity", nullable=false)
    private int deliveredQuantity;

    @Column(name="changed_quantity", nullable=false)
    private int changedQuantity;

    @Id
    @Column(name="article_id", nullable=false)
    private int articleId;

    @Id
    @Column(name="order_id", nullable=false)
    private int orderId;


    public boolean invalid() {
        return plannedQuantity < 0 || deliveredQuantity < 0;
    }

}
