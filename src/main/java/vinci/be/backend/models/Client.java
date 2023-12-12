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
@Entity(name = "clients")
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private int id;

    @Column(name="client_address", nullable=false)
    private String address;

    @Column(name="client_name", nullable=false)
    private String name;

    @Column(name="phone_number", nullable=false)
    private String phoneNumber;

    @Column(name="children_quantity", nullable=false)
    private int childrenQuantity;

    @Column(name="tour", nullable=false)
    private int tour;

    public boolean invalid() {
        return  address == null || address.isBlank() || address.isEmpty()
                || name == null || name.isBlank() || name.isEmpty()
                || phoneNumber == null || phoneNumber.isBlank() || phoneNumber.isEmpty()
                || childrenQuantity <  0 || tour <= 0 ;
    }
}
