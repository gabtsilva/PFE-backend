package vinci.be.backend.services;

import org.springframework.stereotype.Service;
import vinci.be.backend.models.UnsafeCredentials;
import vinci.be.backend.repositories.AuthenticationRepository;

@Service
public class AuthenticationService {
    private final AuthenticationRepository repository;
    public AuthenticationService(AuthenticationRepository repository) {
        this.repository = repository;
    }

    /**
     * Connects user with credentials
     * @param unsafeCredentials The credentials with insecure password
     * @return The JWT token, or null if the user couldn't be connected
     */
    public UnsafeCredentials connect(UnsafeCredentials unsafeCredentials) {
        UnsafeCredentials credentials = repository.findById(unsafeCredentials.getEmail()).orElse(null);
        if(credentials == null || (!credentials.getPassword().equals(unsafeCredentials.getPassword()))) return null;
        return credentials;
    }
}
