package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vinci.be.backend.models.GeneralClientOrder;

import java.util.List;
import java.util.Optional;

public interface GeneralClientOrderRepository extends JpaRepository<GeneralClientOrder, Integer> {

    boolean deleteAllByTourId(int tourId);
    List<GeneralClientOrder> findAllByTourId(int tourId);
    GeneralClientOrder findByClientId(int clientId);

    boolean existsByTourId(int tourId);


    @Query(value = "SELECT MAX(gco.client_order) FROM general_clients_orders gco WHERE gco.tour_id = :tourId", nativeQuery = true)
    Optional<Integer> findMaxClientOrderValueByTourId(@Param("tourId") int tourId);


}
