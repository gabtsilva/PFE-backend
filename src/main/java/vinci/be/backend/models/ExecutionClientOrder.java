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
@Entity(name = "execution_clients_orders")
@Table(name="execution_clients_orders")

public class ExecutionClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execution_client_order_id", nullable = false)
    private int id;

    @Column(name="general_client_order", nullable=false)
    private int generalClientOrderId;

    @Column(name="tour_execution_id", nullable=false)
    private int tourExecutionId;

    @Column(name="delivered", nullable=false)
    private boolean isDelivered;

    public boolean invalid() { return  tourExecutionId < 0 || generalClientOrderId < 0 ;}
}
