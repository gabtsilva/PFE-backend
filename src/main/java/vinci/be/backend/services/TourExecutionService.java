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



        /**
         * Creates an execution for an existing tour in the repository .
         *
         * @param tourId The id of the tour.
         * @param tourExecution the tour execution
         *
         **/

        public void createOneExecution(int tourId, TourExecution tourExecution) throws NotFoundException, ConflitException {
            if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exist");
            if (tourExecutionRepository.existsByExecutionDateAndTourId(tourExecution.getExecutionDate(),tourId)) throw new ConflitException("There is already a tour execution for this tour on the specified date");

            tourExecutionRepository.save(tourExecution);
        }


        /**
         * Retrieves the actual tourExecution of a tour by its state from the repository based on the provided tour ID and state.
         *
         * @param tourId The unique identifier of the tour to be retrieved.
         * @param state The state of the tour execution.
         * @return matching list of tour executions.
         */
        public List<TourExecution> readExecutionByStateForATour(int tourId, String state) throws  NotFoundException {
            if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exist");
            return  tourExecutionRepository.findByTourIdAndState(tourId, state);
        }


        /**
         * Retrieves the  tourExecutions of a tour for a delivery man from the repository based on the provided tour ID and user mail and tour state.
         *
         * @param tourId The unique identifier of the tour to be retrieved.
         * @param userMail The mail of the user.
         * @param state The state of a tour.
         * @return matching list of tour executions.
         */
        public List<TourExecution> readPlannedExecutionByDeliveryManForATour(int tourId, String userMail, String state) throws NotFoundException, BusinessException {
            if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tout does not exist");
            User user = userRepository.findById(userMail).orElse(null);
            if (user == null) throw new NotFoundException("User does not exist");
            if (user.isAdmin() ) throw new BusinessException("User is not a delivery person");

            return  tourExecutionRepository.findByTourIdAndDeliveryPersonAndState(tourId, userMail, state);
        }



    }
