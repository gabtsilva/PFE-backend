package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.SafeCredentials;
import vinci.be.backend.models.UnsafeCredentials;

@Repository
public interface AuthenticationRepository extends JpaRepository<SafeCredentials, String> {
}
