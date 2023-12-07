package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinci.be.backend.models.User;

public interface UserRepository extends JpaRepository<User, String> {
}
