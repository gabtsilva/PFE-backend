package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinci.be.backend.models.Surplus;

public interface SurplusRepository extends JpaRepository<Surplus, SurplusIdentifier> {
}
