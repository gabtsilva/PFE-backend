package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.ExecutionClientOrder;

import java.util.List;

@Repository
public interface ExecutionClientOrderRepository extends JpaRepository<ExecutionClientOrder, Integer> {

    List<ExecutionClientOrder> findAllByTourExecutionId(int tourExecutionId);

    @Query(value = "SELECT EXISTS (\n"
        + "    SELECT 1 \n"
        + "    FROM snappies.execution_clients_orders \n"
        + "    WHERE general_client_order = :generalClienOrderId"
        + "      AND tour_execution_id = :tourId"
        + ");",nativeQuery = true)
  boolean existsByTourIdANDGeneralClientOrder(@Param("tourId") int tourId, @Param("generalClienOrderId") int generalClienOrderId);
}
