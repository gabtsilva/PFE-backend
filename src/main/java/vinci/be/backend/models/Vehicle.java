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
    @Column(name="vehicle_id", nullable=false)
    private int id;

    @Column(name="vehicle_name", nullable=false)
    private String name;

    @Column(name="plate", nullable=false)
    private String plate;

    @Column(name="max_quantity", nullable=false)
    private int maxQuantity;



    public boolean invalid() {
        return name == null || name.isBlank() || name.isEmpty()
                || plate == null || plate.isBlank() || plate.isEmpty()
                || maxQuantity <  0;
    }
}
