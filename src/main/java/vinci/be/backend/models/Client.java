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
    private int client_id;

    @Column(name="client_address", nullable=false)
    private String address;

    @Column(name="client_name", nullable=false)
    private String name;

    @Column(name="phone_number", nullable=false)
    private String phone_number;

    @Column(name="children_quantity", nullable=false)
    private int children_quantity;

    @Column(name="tour", nullable=false)
    private int tour;

    public boolean invalid() {
        return address.isBlank() || address.isEmpty()
                || name.isBlank() || name.isEmpty()
                || phone_number.isBlank() || phone_number.isEmpty()
                || children_quantity <  0;
    }
}
