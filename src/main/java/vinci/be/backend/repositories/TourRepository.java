package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
}
