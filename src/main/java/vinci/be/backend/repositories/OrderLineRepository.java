package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.OrderLine;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, OrderLineIdentifier > {

    List<OrderLine> findByOrderId(int orderId);
}
