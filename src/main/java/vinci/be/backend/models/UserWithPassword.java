package vinci.be.backend.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserWithPassword {
    @JsonProperty("user_data")
    private User user;

    private String password;

    public boolean invalid() {
        return user.invalid() || password == null || password.isBlank();
    }
}
