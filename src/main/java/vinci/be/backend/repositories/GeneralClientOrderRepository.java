package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinci.be.backend.models.GeneralClientOrder;

import java.util.List;

public interface GeneralClientOrderRepository extends JpaRepository<GeneralClientOrder, Integer> {

    boolean deleteAllByTourId(int tourId);
    List<GeneralClientOrder> findAllByTourId(int tourId);

}
