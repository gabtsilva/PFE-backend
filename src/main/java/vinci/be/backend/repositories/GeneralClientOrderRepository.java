package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinci.be.backend.models.GeneralClientOrder;

public interface GeneralClientOrderRepository extends JpaRepository<GeneralClientOrder, Integer> {
}
