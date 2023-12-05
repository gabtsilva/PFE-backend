package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.TextHelloWorld;

import java.util.List;

@Repository
public interface HelloWorldRepository extends JpaRepository<TextHelloWorld, Integer> {
    @Query("SELECT t FROM texte t")
    List<TextHelloWorld> methodPerso();
}
