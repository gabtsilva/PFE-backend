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
@Entity(name = "vehicles")
@Table(name="vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicle_id;

    @Column(name="vehicle_name", nullable=false)
    private String name;

    @Column(name="plate", nullable=false)
    private String plate;

    @Column(name="max_quantity", nullable=false)
    private int max_quantity;



    public boolean invalid() {
        return name.isBlank() || name.isEmpty()
                || plate.isBlank() || plate.isEmpty()
                || max_quantity <  0;
    }
}
