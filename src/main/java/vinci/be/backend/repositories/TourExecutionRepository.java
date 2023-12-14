package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinci.be.backend.models.TourExecution;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourExecutionRepository extends JpaRepository<TourExecution, Integer> {
  boolean existsByExecutionDateAndTourId(LocalDate executionDate, int tour);

  List<TourExecution> findByTourIdAndState(int tourId, String state);

  List<TourExecution> findByTourIdAndDeliveryPersonAndState(int tourId, String userMail, String state);

  @Query(value = "SELECT\n"
      + "    a.article_id AS \"articleId\",\n"
      + "    a.article_name AS \"articleName\",\n"
      + "    SUM(ol.planned_quantity) AS \"totalPlannedQuantity\",\n"
      + "    SUM(ol.changed_quantity) AS \"totalChangedQuantity\",\n"
      + "    COALESCE(MAX(a.pourcentage), 0) AS \"surplusPercentage\"\n"
      + "FROM\n"
      + "    snappies.articles a\n"
      + "    INNER JOIN snappies.orders_lines ol ON a.article_id = ol.article_id\n"
      + "    INNER JOIN snappies.orders o ON ol.order_id = o.order_id\n"
      + "    INNER JOIN snappies.general_clients_orders gco ON o.client_id = gco.client_id\n"
      + "    INNER JOIN snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order\n"
      + "WHERE\n"
      + "    eco.tour_execution_id = :idExecutionTournee -- Remplacez [VotreTourExecutionId] par l'ID spécifique du tour d'exécution\n"
      + "GROUP BY\n"
      + "    a.article_id;",
      nativeQuery = true)
  List<Object[]> getAllArticles(@Param("idExecutionTournee") int idExecutionTournee);


  @Query(value = "SELECT c.client_id, c.client_address, c.client_name, c.phone_number, c.children_quantity, c.tour FROM snappies.clients c WHERE c.tour IN (SELECT tour_id FROM snappies.tours_executions WHERE tour_execution_id = :idExecutionTournee)", nativeQuery = true)
  List<Object[]> getAllClients(@Param("idExecutionTournee") int idExecutionTournee);

  @Query(value = "SELECT\n"
      + "  art.article_id,\n"
      + "  art.article_name,\n"
      + "  ol.planned_quantity,\n"
      + "  ol.delivered_quantity,\n"
      + "  ol.changed_quantity\n"
      + "  FROM\n"
      + "  snappies.articles art\n"
      + "  INNER JOIN snappies.orders_lines ol ON art.article_id = ol.article_id\n"
      + "  INNER JOIN snappies.orders ord ON ol.order_id = ord.order_id\n"
      + "  INNER JOIN snappies.general_clients_orders gco ON ord.client_id = gco.client_id\n"
      + "  INNER JOIN snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order\n"
      + "  INNER JOIN snappies.tours_executions tex ON eco.tour_execution_id = tex.tour_execution_id\n"
      + "      WHERE\n"
      + "  tex.tour_execution_id = :idExecutionTournee -- Remplacez :tourExecutionId par l'ID de l'exécution de tournée spécifique\n"
      + "  AND gco.client_id = :clientId", nativeQuery = true)
  List<Object[]> getAllArticlesByClients(@Param("idExecutionTournee") int idExecutionTournee, @Param("clientId") int idClient);



  @Modifying
  @Query(value = "UPDATE snappies.orders_lines\n"
      + "SET delivered_quantity = :delivredQty \n"
      + "FROM snappies.orders ord \n"
      + "INNER JOIN snappies.general_clients_orders gco ON ord.client_id = gco.client_id \n"
      + "INNER JOIN snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order \n"
      + "WHERE orders_lines.article_id =  :articleId\n"
      + "AND ord.order_id = orders_lines.order_id \n"
      + "AND eco.tour_execution_id = :idExecutionTournee \n"
      + "AND gco.client_id = :clientId\n", nativeQuery = true)
  void updateComandeClientTourExecuution(@Param("idExecutionTournee") int idExecutionTournee,@Param("clientId") int clientId, @Param("articleId") int articleId,@Param("delivredQty") double delivredQty);
  @Modifying
  @Query(value = "UPDATE snappies.execution_clients_orders eco SET delivered = :delivred FROM snappies.general_clients_orders gco WHERE eco.general_client_order = gco.general_client_order_id AND eco.tour_execution_id = :idExecutionTournee AND gco.client_id = :clientId ",nativeQuery = true)
  void updateRealiser(@Param("idExecutionTournee") int idExecutionTournee,@Param("clientId") int clientId, @Param("delivred") boolean delivred);

  @Query(value = "SELECT t.tour_execution_id, t.tour_id, t.state, t.vehicle_id, t.execution_date, t.delivery_person\n"
      + "FROM snappies.tours_executions t\n"
      + "WHERE (delivery_person = COALESCE(:deliveryPerson, delivery_person))\n"
      + "AND (TO_DATE(:executionDate, 'YYYY-MM-DD') IS NULL OR execution_date = COALESCE(TO_DATE(:executionDate, 'YYYY-MM-DD'), execution_date))", nativeQuery = true)
  List<Object[]> getAllTourExecutionForLocalDate(@Param("executionDate") LocalDate executionDate, @Param("deliveryPerson") String deliveryPerson);

  @Query(value = "SELECT t.tour_execution_id, t.tour_id, t.state, t.vehicle_id, t.execution_date, t.delivery_person\n"
      + "FROM snappies.tours_executions t\n"
      + "WHERE (t.state = COALESCE(:state, t.state))\n"
      + "AND (TO_DATE(:executionDate, 'YYYY-MM-DD') IS NULL OR execution_date = COALESCE(TO_DATE(:executionDate, 'YYYY-MM-DD'), execution_date))", nativeQuery = true)
  List<Object[]> getAllTourExecutionForLocalDateAndState(@Param("executionDate")LocalDate executionDate,@Param("state") String state);

  @Query(value = " SELECT\n"
      + "    c.client_name AS \"ClientName\",\n"
      + "    eco.delivered AS \"Delivered\"\n"
      + "FROM\n"
      + "    snappies.clients c\n"
      + "JOIN\n"
      + "    snappies.general_clients_orders gco ON c.client_id = gco.client_id\n"
      + "JOIN\n"
      + "    snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order\n"
      + "WHERE\n"
      + "    eco.tour_execution_id = :tourExecutionId" ,nativeQuery = true)
  List<Object[]> getClientDeliveredBool(@Param("tourExecutionId") int tourExecutionId);




  @Query(value = "SELECT\n"
      + "    a.article_id AS \"articleId\",\n"
      + "    a.article_name AS \"articleName\",\n"
      + "    SUM(ol.planned_quantity) AS \"totalPlannedQuantity\",\n"
      + "    SUM(ol.changed_quantity) AS \"totalChangedQuantity\",\n"
      + "    SUM(ol.delivered_quantity) AS \"delivered qty \", \n"
      + "    COALESCE(MAX(a.pourcentage), 0) AS \"surplusPercentage\"\n"
      + "FROM\n"
      + "    snappies.articles a\n"
      + "    INNER JOIN snappies.orders_lines ol ON a.article_id = ol.article_id\n"
      + "    INNER JOIN snappies.orders o ON ol.order_id = o.order_id\n"
      + "    INNER JOIN snappies.general_clients_orders gco ON o.client_id = gco.client_id\n"
      + "    INNER JOIN snappies.execution_clients_orders eco ON gco.general_client_order_id = eco.general_client_order\n"
      + "WHERE\n"
      + "    eco.tour_execution_id = :idExecutionTournee -- Remplacez [VotreTourExecutionId] par l'ID spécifique du tour d'exécution\n"
      + "GROUP BY\n"
      + "    a.article_id;",
      nativeQuery = true)
  List<Object[]> getAllArticlesALL(@Param("idExecutionTournee") int idExecutionTournee);

}


