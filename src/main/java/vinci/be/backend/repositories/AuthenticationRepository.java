package vinci.be.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.SafeCredentials;
import vinci.be.backend.models.UnsafeCredentials;

@Repository
public interface AuthenticationRepository extends CrudRepository<SafeCredentials, String> {
}
