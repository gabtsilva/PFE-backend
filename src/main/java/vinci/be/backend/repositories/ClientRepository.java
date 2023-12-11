package vinci.be.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.Client;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT c FROM clients c WHERE c.tour = ?1")
    List<Client> findAllByIdTour(int xxx);
}
