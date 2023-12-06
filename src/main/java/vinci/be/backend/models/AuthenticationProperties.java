package vinci.be.backend.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vinci.be.backend")
public class AuthenticationProperties {
    private String secret;
}
