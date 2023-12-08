package vinci.be.backend.services;

import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.ConflitException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.TourExecution;
import vinci.be.backend.models.User;
import vinci.be.backend.repositories.TourExecutionRepository;
import vinci.be.backend.repositories.TourRepository;
import vinci.be.backend.repositories.UserRepository;

import java.util.List;

@Service
public class TourExecutionService {

  private final TourExecutionRepository tourExecutionRepository;
  private final TourRepository tourRepository;
  private final UserRepository userRepository;

  public TourExecutionService(TourExecutionRepository tourExecutionRepository, TourRepository tourRepository, UserRepository userRepository) {
    this.tourExecutionRepository = tourExecutionRepository;
    this.tourRepository = tourRepository;
    this.userRepository = userRepository;
  }

  public void createOneExecution(int tourId, TourExecution tourExecution) throws NotFoundException, ConflitException {
    if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exist");
    if (tourExecutionRepository.existsByExecutionDateAndTourId(tourExecution.getExecutionDate(),tourId)) throw new ConflitException("There is already a tour execution for this tour on the specified date");

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

}
