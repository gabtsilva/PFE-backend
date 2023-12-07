package vinci.be.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnsafeCredentials {
    private String mail;
    private String password;

    public SafeCredentials makeSafe(String hashedPassword) {
        return new SafeCredentials(mail, hashedPassword);
    }

    public boolean invalid() {
        return mail == null || mail.isBlank() ||  mail.isEmpty() ||
                password == null || password.isBlank() || password.isEmpty();
    }
}
