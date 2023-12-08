package vinci.be.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findByClientId(int clientId);
}
