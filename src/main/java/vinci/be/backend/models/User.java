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
@Entity(name = "users")
@Table(name="users")
public class User {
    @Id
    @Column(name="email")
    private String email;

    @Column(name="firstname", nullable=false)
    private String firstname;

    @Column(name="lastname", nullable=false)
    private String lastname;

    @Column(name="phone_number", nullable=false)
    private String phoneNumber;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="is_admin", nullable=false)
    private boolean isAdmin;


    public boolean isDeliveryMan() {return !isAdmin;}

    public boolean invalid() {
        return  email == null || email.isBlank() || email.isEmpty()
                || firstname == null || firstname.isBlank() || firstname.isEmpty()
                || lastname == null || lastname.isBlank() || lastname.isEmpty()
                || phoneNumber.isBlank() || phoneNumber.isEmpty();
    }
}
