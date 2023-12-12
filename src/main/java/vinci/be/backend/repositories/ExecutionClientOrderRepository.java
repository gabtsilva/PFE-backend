package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.ExecutionClientOrder;

import java.util.List;

@Repository
public interface ExecutionClientOrderRepository extends JpaRepository<ExecutionClientOrder, Integer> {

    List<ExecutionClientOrder> findAllByTourExecutionId(int tourExecutionId);
}
