package vinci.be.backend.services;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.AllArticlesTourExecution;
import vinci.be.backend.models.ArticlesCommande;
import vinci.be.backend.models.Client;
import vinci.be.backend.models.OrderLine;
import vinci.be.backend.models.Surplus;
import vinci.be.backend.models.TourExecution;
import vinci.be.backend.models.User;
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

  public TourExecutionService(TourExecutionRepository tourExecutionRepository,
      TourRepository tourRepository, UserRepository userRepository,
      VehicleRepository vehicleRepository, ArticleRepository articleRepository, SurplusRepository surplusRepository, ClientRepository clientRepository) {
    this.tourExecutionRepository = tourExecutionRepository;
    this.tourRepository = tourRepository;
    this.userRepository = userRepository;
    this.vehicleRepository = vehicleRepository;
    this.articleRepository = articleRepository;
    this.surplusRepository = surplusRepository;
    this.clientRepository = clientRepository;
  }

  public void createOneExecution(int tourId, TourExecution tourExecution)
      throws NotFoundException {
    if (!tourRepository.existsById(tourId))
      throw new NotFoundException("Tour does not exist");
    tourExecutionRepository.existsByExecutionDateAndTourId(tourExecution.getExecutionDate(),tourId);
    tourExecutionRepository.save(tourExecution);
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
  }

  public void addSurplus(int tourExecutionId, Surplus surplus) throws NotFoundException {
    if (!tourExecutionRepository.existsById(tourExecutionId)) throw new NotFoundException("Tour execution does not exist");
    if (!articleRepository.existsById(surplus.getArticleId())) throw new NotFoundException("Article does not exist");

    surplusRepository.save(surplus);

  }

  public List<AllArticlesTourExecution> getAllArticles(int tourExecutionId) throws NotFoundException {
    TourExecution tourExecution = tourExecutionRepository.getReferenceById(tourExecutionId);
    if (tourExecution == null){
      throw new NotFoundException("tour not found");
    }
    List<AllArticlesTourExecution> results = new ArrayList<>();
    for (Object[] row : tourExecutionRepository.getAllArticles(tourExecutionId)) {
      AllArticlesTourExecution article = new AllArticlesTourExecution();
      article.setId((Integer) row[0]);
      article.setName((String) row[1]);
      article.setPlanned_quantity((Double) row[2]);
      article.setTotal_with_surplus((Double) row[3]);
      results.add(article);
    }
    return results;
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



}
