package vinci.be.backend.services;

import java.util.random.RandomGenerator.ArbitrarilyJumpableGenerator;
import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
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

  public TourExecutionService(TourExecutionRepository tourExecutionRepository,
      TourRepository tourRepository, UserRepository userRepository,
      VehicleRepository vehicleRepository, ArticleRepository articleRepository, SurplusRepository surplusRepository) {
    this.tourExecutionRepository = tourExecutionRepository;
    this.tourRepository = tourRepository;
    this.userRepository = userRepository;
    this.vehicleRepository = vehicleRepository;
    this.articleRepository = articleRepository;
    this.surplusRepository = surplusRepository;
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
    if (actualSate.equals("prevue") && state.equals("commencee")){
      tourExecution.setState(state);
      tourExecutionRepository.save(tourExecution);

    }else if (actualSate.equals("commencee") && state.equals("finie")){
      tourExecution.setState(state);
      tourExecutionRepository.save(tourExecution);
    }
    throw new IllegalArgumentException("not in good state");
  }

  public void addSurplus(int tourExecutionId, Surplus surplus) throws NotFoundException {
    if (!tourExecutionRepository.existsById(tourExecutionId)) throw new NotFoundException("Tour execution does not exist");
    if (!articleRepository.existsById(surplus.getArticleId())) throw new NotFoundException("Article does not exist");

    surplusRepository.save(surplus);

  }
}
