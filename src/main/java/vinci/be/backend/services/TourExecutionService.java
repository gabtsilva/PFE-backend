package vinci.be.backend.services;

import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.TourExecution;
import vinci.be.backend.models.User;
import vinci.be.backend.repositories.TourExecutionRepository;
import vinci.be.backend.repositories.TourRepository;
import vinci.be.backend.repositories.UserRepository;

import java.util.List;
import vinci.be.backend.repositories.VehicleRepository;

@Service
public class TourExecutionService {

  private final TourExecutionRepository tourExecutionRepository;
  private final TourRepository tourRepository;
  private final UserRepository userRepository;

  private final VehicleRepository vehicleRepository;

  public TourExecutionService(TourExecutionRepository tourExecutionRepository,
      TourRepository tourRepository, UserRepository userRepository,
      VehicleRepository vehicleRepository) {
    this.tourExecutionRepository = tourExecutionRepository;
    this.tourRepository = tourRepository;
    this.userRepository = userRepository;
    this.vehicleRepository = vehicleRepository;
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

}
