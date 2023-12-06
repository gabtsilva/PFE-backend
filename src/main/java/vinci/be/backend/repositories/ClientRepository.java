package vinci.be.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
