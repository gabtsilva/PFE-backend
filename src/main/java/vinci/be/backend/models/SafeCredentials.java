package vinci.be.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SafeCredentials {
    public SafeCredentials(String mail, String hashedPassword){
        this.mail = mail;
        this.hashedPassword = hashedPassword;
    }

    @Id
    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "password", nullable = false)
    private String hashedPassword;

    @Column(name = "is_admin", nullable = false)
    private boolean is_admin;
}