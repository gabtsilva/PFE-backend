package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.Client;
import vinci.be.backend.models.TourExecution;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourExecutionRepository extends JpaRepository<TourExecution, Integer> {
  boolean existsByExecutionDateAndTourId(LocalDate executionDate, int tour);

  List<TourExecution> findByTourIdAndState(int tourId, String state);

  List<TourExecution> findByTourIdAndDeliveryPersonAndState(int tourId, String userMail, String state);

  @Query(value = "SELECT art.article_id, art.article_name, SUM(ol.planned_quantity) AS total_planned_quantity, " +
      "SUM(ol.planned_quantity) * (1 + COALESCE(MAX(s.percentage), 0) / 100.0) AS total_with_surplus " +
      "FROM snappies.articles art " +
      "INNER JOIN snappies.orders_lines ol ON art.article_id = ol.article_id " +
      "INNER JOIN snappies.orders ord ON ol.order_id = ord.order_id " +
      "INNER JOIN snappies.clients cli ON ord.client_id = cli.client_id " +
      "INNER JOIN snappies.tours_executions tex ON cli.tour = tex.tour_id " +
      "LEFT JOIN snappies.surplus s ON art.article_id = s.article_id AND tex.tour_execution_id = s.tour_execution_id " +
      "WHERE tex.tour_execution_id = :idExecutionTournee " +
      "GROUP BY art.article_id, art.article_name",
      nativeQuery = true)
  List<Object[]> getAllArticles(@Param("idExecutionTournee") int idExecutionTournee);


  @Query(value = "SELECT c.client_id, c.client_address, c.client_name, c.phone_number, c.children_quantity, c.tour FROM snappies.clients c WHERE c.tour IN (SELECT tour_id FROM snappies.tours_executions WHERE tour_execution_id = :idExecutionTournee)", nativeQuery = true)
  List<Object[]> getAllClients(@Param("idExecutionTournee") int idExecutionTournee);

}
