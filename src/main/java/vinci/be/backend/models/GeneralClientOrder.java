package vinci.be.backend.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "general_clients_orders")
@Table(name="general_clients_orders")

public class GeneralClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="general_client_order_id", nullable=false)
    private int id;

    @Column(name="client_id", nullable=false)
    private int clientId;

    @Column(name="tour_id", nullable=false)
    private int tourId;

    @Column(name="client_order", nullable=false)
    private int order;

    public boolean invalid() { return  clientId < 0 || tourId < 0 || order < 0;}
}
