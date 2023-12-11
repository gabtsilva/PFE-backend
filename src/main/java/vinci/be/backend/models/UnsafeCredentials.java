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
    private String email;
    private String password;
    private boolean is_admin;

    /*
    public SafeCredentials makeSafe(String hashedPassword) {
        return new SafeCredentials(email, hashedPassword, false);
    }

     */

    public boolean invalid() {
        return email == null || email.isBlank() ||  email.isEmpty() ||
                password == null || password.isBlank() || password.isEmpty();
    }
}
