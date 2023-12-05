package vinci.be.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class UnsafeCredentials {

    @Id
    private String email;
    @Column(name="password", nullable=false)
    private String password;

    public boolean invalid() {
        return email.isEmpty() || password.isEmpty();
    }
}
