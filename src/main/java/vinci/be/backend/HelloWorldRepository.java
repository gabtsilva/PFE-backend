package vinci.be.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelloWorldRepository extends JpaRepository<TextHelloWorld, Integer> {
    @Query("SELECT t FROM texte t")
    List<TextHelloWorld> methodPerso();
}
