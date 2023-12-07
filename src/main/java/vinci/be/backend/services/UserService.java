package vinci.be.backend.services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sun.misc.Unsafe;
import vinci.be.backend.models.UnsafeCredentials;
import vinci.be.backend.models.User;
import vinci.be.backend.models.UserWithPassword;
import vinci.be.backend.repositories.UserRepository;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService  = authenticationService;
    }

    /**
     * Reads all users in repository
     *
     * @return all users
     */
    public List<User> readAll() {
        return userRepository.findAll();
    }


    /**
     * Retrieves a specific user from the repository based on the provided user mail.
     *
     * @param userMail The unique mail of the user to be retrieved.
     * @return The user object corresponding to the provided user mail, or null if not found.
     */
    public User readOne(String userMail) {
        return userRepository.findById(userMail).orElse(null);
    }


    /**
     * Creates a new user in the repository if the provided user mail does not already exist.
     *
     * @param userWithPassword The user object to be created.
     * @return true if the user is successfully created, false if a user with the same mail already exists.
     */
    public boolean createOne(UserWithPassword userWithPassword) {
        if (userRepository.existsById(userWithPassword.getUser().getMail())) return false;
        UnsafeCredentials unsafeCredentials = new UnsafeCredentials(userWithPassword.getUser().getMail(), userWithPassword.getPassword());

        authenticationService.createOne(unsafeCredentials);
        userRepository.save(userWithPassword.getUser());
        return true;
    }

    /**
     * Updates an existing user in the repository if the provided user mail already exists.
     *
     * @param user The user object with updated information.
     * @return true if the user is successfully updated, false if the user with the provided mail does not exist.
     */
    public boolean updateOne(User user) {
        if (!userRepository.existsById(user.getMail())) return false;
        userRepository.save(user);
        return true;
    }
}
