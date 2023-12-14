package vinci.be.backend.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.*;
import vinci.be.backend.repositories.*;

import java.util.List;

@Service
public class TourExecutionService {

    private final TourExecutionRepository tourExecutionRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    private final SurplusRepository surplusRepository;

    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final GeneralClientOrderRepository generalClientOrderRepository;

    private final ExecutionClientOrderRepository executionClientOrderRepository;

    public TourExecutionService(TourExecutionRepository tourExecutionRepository,
                                TourRepository tourRepository, UserRepository userRepository,
                                VehicleRepository vehicleRepository, ArticleRepository articleRepository, SurplusRepository surplusRepository, ClientRepository clientRepository, ExecutionClientOrderRepository executionClientOrderRepository,
                                GeneralClientOrderRepository generalClientOrderRepository) {
        this.tourExecutionRepository = tourExecutionRepository;
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.articleRepository = articleRepository;
        this.surplusRepository = surplusRepository;
        this.clientRepository = clientRepository;
        this.executionClientOrderRepository = executionClientOrderRepository;
        this.generalClientOrderRepository = generalClientOrderRepository;
    }

    public void createOneExecution(int tourId, TourExecution tourExecution)
            throws NotFoundException {
        if (!tourRepository.existsById(tourId))
            throw new NotFoundException("Tour does not exist");
        tourExecutionRepository.existsByExecutionDateAndTourId(tourExecution.getExecutionDate(), tourId);
        TourExecution tourExecution1 = tourExecutionRepository.save(tourExecution);
        //tourExecutionRepository.createExecClientOrders(tourExecution1.getId(), tourId, );

    }

  public List<TourExecution> readExecutionByStateForATour(int tourId, String state) throws  NotFoundException {
    if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exist");
    return  tourExecutionRepository.findByTourIdAndState(tourId, state);
  }

  public List<TourExecution> readPlannedExecutionByDeliveryManForATour(int tourId, String userMail, String state) throws NotFoundException, BusinessException {
    if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tout does not exist");
    User user = userRepository.findById(userMail).orElse(null);
    if (user == null) throw new NotFoundException("User does not exist");
    if (user.isAdmin() ) throw new BusinessException("User is not a delivery person");

    return  tourExecutionRepository.findByTourIdAndDeliveryPersonAndState(tourId, userMail, state);
  }

  public void updatedeliveryPersonExecution(int tourId, String deliveryPerson)
      throws NotFoundException {
    if (!userRepository.existsById(deliveryPerson)) {
      throw new NotFoundException("User does not exist");
    }
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourId);
    if (tourExecution == null) {
      throw new NotFoundException("TourExecution does not exist");
    }
    tourExecution.setDeliveryPerson(deliveryPerson);
    tourExecutionRepository.save(tourExecution);
  }

  public void updateVanExecution(int tourId, int vanId) throws NotFoundException {
    if (!vehicleRepository.existsById(vanId)) {
      throw new NotFoundException("Van does not exist");
    }
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourId);
    if (tourExecution == null) {
      throw new NotFoundException("TourExecution does not exist");
    }
    tourExecution.setVehicleId(vanId);
    tourExecutionRepository.save(tourExecution);
  }

  public void updateState(int tourId, String state) throws NotFoundException {

    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourId);
    if (tourExecution == null){
      throw new NotFoundException("tour not found");
    }

    tourExecution.setState(state);
    tourExecutionRepository.save(tourExecution);/*
    String actualSate = tourExecution.getState();
    if (actualSate.equals("prévue") && state.equals("commencée")){
      tourExecution.setState(state);
      tourExecutionRepository.save(tourExecution);
    }else if (actualSate.equals("commencée") && state.equals("finie")){
      tourExecution.setState(state);
      tourExecutionRepository.save(tourExecution);
    }else {
    throw new IllegalArgumentException("not in good state");
    }
    */
  }

  public void addSurplus(int tourExecutionId, Surplus surplus) throws NotFoundException {
    if (!tourExecutionRepository.existsById(tourExecutionId)) throw new NotFoundException("Tour execution does not exist");
    if (!articleRepository.existsById(surplus.getArticleId())) throw new NotFoundException("Article does not exist");

    surplusRepository.save(surplus);

  }

  public List<AllArticlesTourExecution> getAllArticles(int tourExecutionId) throws NotFoundException {
      try {
        TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourExecutionId);
        if (tourExecution == null) {
          throw new NotFoundException("tour not found");
        }
        List<AllArticlesTourExecution> results = new ArrayList<>();
        for (Object[] row : tourExecutionRepository.getAllArticles(tourExecutionId)) {
          AllArticlesTourExecution article = new AllArticlesTourExecution();
          article.setId((Integer) row[0]);
          article.setName((String) row[1]);
          double planedQtyBase = (Double) row[2];
          double changedQtyBase = (Double) row[3];
          article.setPlanned_quantity(changedQtyBase);
          double totalAvecSurplus = changedQtyBase * (1 + (Double) row[4]);
          article.setTotal_with_surplus( roundToNearestHalfOrOne( totalAvecSurplus));
          results.add(article);
        }
        return results;
      }catch (Exception e){
        System.out.println(e.getMessage());
      }
      return null;
  }

  public List<Client> getAllClients(int tourExecutionId)
      throws NotFoundException {
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourExecutionId);
    if (tourExecution == null){
      throw new NotFoundException("tour not found");
    }
    List<Client> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getAllClients(tourExecutionId)) {
      System.out.println("---");
      Client client = new Client();
      client.setId((Integer) row[0]);
      client.setAddress((String) row[1]);
      client.setName((String) row[2]);
      client.setAddress((String) row[3]);
      client.setPhoneNumber((String) row[3]);
      client.setChildrenQuantity((int) row[4]);
      client.setTour((int) row[5]);
      results.add(client);

    }
    return results;
  }


  public List<ArticlesCommande> getAllArticlesByClients(int tourExecutionId, int idClient)
      throws NotFoundException {
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourExecutionId);
    Client client= clientRepository.getReferenceById(idClient);
    if (tourExecution == null){
      throw new NotFoundException("tour not found");
    }
    if (client == null){
      throw new NotFoundException("client not found");
    }
    List<ArticlesCommande> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getAllArticlesByClients(tourExecutionId, idClient)) {
      ArticlesCommande orderLine = new ArticlesCommande();
      orderLine.setArticleId((int) row[0]);
      orderLine.setArticleName((String) row[1]);
      orderLine.setPlannedQuantity((double) row[2]);
      orderLine.setDeliveredQuantity((double) row[3]);
      orderLine.setChangedQuantity((double) row[4]);
      results.add(orderLine);

    }
    return results;
  }

  @Transactional
  public void updateComandeClientTourExecuution(int tourExecutionId, int clientId, List<ArticlesCommande> articlesCommandeList)
      throws NotFoundException {
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourExecutionId);
    Client client= clientRepository.getReferenceById(clientId);
    if (tourExecution == null){
      throw new NotFoundException("tour not found");
    }
    if (client == null){
      throw new NotFoundException("client not found");
    }
    for (ArticlesCommande ac:articlesCommandeList) {

        tourExecutionRepository.updateComandeClientTourExecuution(tourExecutionId, clientId,
              ac.getArticleId(), ac.getDeliveredQuantity());
    }
    tourExecutionRepository.updateRealiser(tourExecutionId, clientId, true);

  }

    /**
     * read tour execution order
     *
     * @param tourExecutionId the id of the tour
     * @return tour execution order
     */
    @Transactional
    public List<ExecutionClientOrder> readTourExecutionOrder(int tourExecutionId) throws NotFoundException {
        if (!tourExecutionRepository.existsById(tourExecutionId)) throw new NotFoundException("Tour execution does not exists");
        return executionClientOrderRepository.findAllByTourExecutionId(tourExecutionId);
    }


    /**
     * create tour execution order
     *
     * @param tourExecutionId the id of the tour
     * @return tour execution order
     */
    public List<ExecutionClientOrder> createClientExecutionOrder(int tourId, int tourExecutionId) throws NotFoundException {
        if (!tourRepository.existsById(tourExecutionId)) throw new NotFoundException("Tour execution does not exists");
        for (GeneralClientOrder generalOrder : generalClientOrderRepository.findAllByTourId(tourId)) {
            ExecutionClientOrder executionClientOrder = new ExecutionClientOrder();
            executionClientOrder.setGeneralClientOrderId(generalOrder.getId());
            executionClientOrder.setTourExecutionId(tourExecutionId);
            executionClientOrder.setDelivered(false);
            executionClientOrderRepository.save(executionClientOrder);

        }
        return executionClientOrderRepository.findAllByTourExecutionId(tourId);

    }


  public List<TourExecution> getTourByidDeliveryPersonForDate(String idDeliveryPerson, LocalDate executionDate) {
    List<TourExecution> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getAllTourExecutionForLocalDate(executionDate,idDeliveryPerson)) {
      TourExecution tourExecution = new TourExecution();
      tourExecution.setId((int) row[0]);
      tourExecution.setTourId((int) row[1]);
      tourExecution.setState((String) row[2]);
      tourExecution.setVehicleId((int) row[3]);
      tourExecution.setExecutionDate(((Date) row[4]).toLocalDate()); // Conversion de java.sql.Date en java.time.LocalDate
      tourExecution.setDeliveryPerson((String) row[5]);
      results.add(tourExecution);

    }
    return results;
  }
  public List<TourExecution> getTourByStateForDate(LocalDate executionDate, String state) {
    List<TourExecution> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getAllTourExecutionForLocalDateAndState(executionDate,state)) {
      TourExecution tourExecution = new TourExecution();
      tourExecution.setId((int) row[0]);
      tourExecution.setTourId((int) row[1]);
      tourExecution.setState((String) row[2]);
      tourExecution.setVehicleId((int) row[3]);
      tourExecution.setExecutionDate(((Date) row[4]).toLocalDate()); // Conversion de java.sql.Date en java.time.LocalDate
      tourExecution.setDeliveryPerson((String) row[5]);
      results.add(tourExecution);

    }
    return results;
  }

  private static double roundToNearestHalfOrOne(double number) {
    double roundedValue = Math.round(number * 2) / 2.0; // Multiplie par 2 pour effectuer l'arrondi à 0,5, puis divise par 2 pour obtenir la valeur arrondie
    return Math.max(roundedValue, 1.0); // Prend la valeur la plus grande entre la valeur arrondie et 1
  }


  public List<ClientDelivered> getClientDeliveredBool(int tourExecutionId) {
    List<ClientDelivered> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getClientDeliveredBool(tourExecutionId)) {
      ClientDelivered clientDelivered  = new ClientDelivered();
      clientDelivered.setName((String) row[0]);
      clientDelivered.setDelivred((boolean) row[1]);
      results.add(clientDelivered);
    }
    return results;
  }

  public List<ArticlesDelivred> getAllArticlesQty(int tourExecutionId) {
      return null;
  }
}
