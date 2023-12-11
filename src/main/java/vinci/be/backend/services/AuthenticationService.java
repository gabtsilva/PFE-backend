package vinci.be.backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;;
import vinci.be.backend.models.UnsafeCredentials;
import vinci.be.backend.models.User;
import vinci.be.backend.repositories.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    public AuthenticationService(UserRepository userRepository) {
        Properties propertiesFile = new Properties();
        String secret = null;
        try (FileInputStream fileInputStream = new FileInputStream("dev.properties")) {
            propertiesFile.load(fileInputStream);
            secret = propertiesFile.getProperty("jwt.secret");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.userRepository = userRepository;
        this.algorithm = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.algorithm).withIssuer("auth0").build();
    }

    /**
     * Connects user with credentials
     * @param unsafeCredentials The credentials with insecure password
     * @return The JWT token, or null if the user couldn't be connected
     */
    public String connect(UnsafeCredentials unsafeCredentials) {
        User user = userRepository.findById(unsafeCredentials.getEmail()).orElse(null);
        if(user == null || (!BCrypt.checkpw(unsafeCredentials.getPassword(), user.getPassword()))) return null;
        return JWT.create().withIssuer("auth0").withClaim("email", user.getEmail()).withClaim("is_admin",user.isAdmin()).sign(this.algorithm);
    }


    /**
     * Verifies JWT token
     * @param token The JWT token
     * @return The pseudo of the user, or null if the token couldn't be verified
     */
    public String verify(String token) {
        try {
            String email = verifier.verify(token).getClaim("email").asString();
            if (!userRepository.existsById(email)) return null;
            return email;
        } catch (JWTVerificationException e) {
            System.err.println("Erreur lors de la vérification du jeton : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

/*
    /**
     * Creates credentials in repository
     * @param unsafeCredentials The credentials with insecure password
     * @return True if the credentials were created, or false if they already exist

    public boolean createOne(UnsafeCredentials unsafeCredentials) {
        if (userRepository.existsById(unsafeCredentials.getEmail())) return false;
        String hashedPassword = BCrypt.hashpw(unsafeCredentials.getPassword(), BCrypt.gensalt());
        //userRepository.save(unsafeCredentials.makeSafe(hashedPassword));
        return true;
    }
*/

}
