//package vinci.be.backend.repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import vinci.be.backend.models.Tour;
//import vinci.be.backend.models.TourExecution;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Repository
//public interface TourExecutionRepository extends JpaRepository<TourExecution, Integer> {
//    boolean existsByExecutionDateAndTourId(LocalDate executionDate, int tour);
//
//    List<TourExecution> findByTourIdAndState(int tourId, String state);
//
//    List<TourExecution> findByTourIdAndDeliveryPersonAndState(int tourId, String userMail, String state);
//
//}
//
