package vinci.be.backend.services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.GeneralClientOrder;
import vinci.be.backend.models.Tour;
import vinci.be.backend.repositories.ClientRepository;
import vinci.be.backend.repositories.GeneralClientOrderRepository;
import vinci.be.backend.repositories.OrderRepository;
import vinci.be.backend.repositories.TourRepository;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;
    private final ClientRepository clientRepository;
    private final GeneralClientOrderRepository generalClientOrderRepository;

    public TourService(TourRepository tourRepository, ClientRepository clientRepository, GeneralClientOrderRepository generalClientOrderRepository) {
        this.tourRepository = tourRepository;
        this.clientRepository = clientRepository;
        this.generalClientOrderRepository = generalClientOrderRepository;
    }

    /**
     * Reads all tours in repository
     *
     * @return all tours
     */
    public List<Tour> readAll() {
        return tourRepository.findAll();
    }


    /**
     * Retrieves a specific tour from the repository based on the provided tour ID.
     *
     * @param tourId The unique identifier of the tourId to be retrieved.
     * @return The tour object corresponding to the provided tourId , or null if not found.
     */
    public Tour readOne(int tourId) {
        return tourRepository.findById(tourId).orElse(null);
    }


    /**
     * Creates a new tour in the repository if the provided tour ID does not already exist.
     *
     * @param tour The tour object to be created.
     * @return true if the tour is successfully created, false if a tour with the same ID already exists.
     */
    public Tour createOne(Tour tour) {
        if (tourRepository.existsById(tour.getId())) return null;
        tourRepository.save(tour);
        return tour;
    }

    /**
     * Updates an existing tour in the repository if the provided tour ID already exists.
     *
     * @param tour The tour object with updated information.
     * @return true if the tour is successfully updated, false if the tour with the provided ID does not exist.
     */
    public boolean updateOne(Tour tour) {
        if (!tourRepository.existsById(tour.getId())) return false;
        tourRepository.save(tour);
        return true;
    }


    /**
     * creates the general delivery order for customers
     *
     * @param tourId the id of the tour
     * @param generalClientsOrders the order
     */
    public void createOrder(int  tourId, List<GeneralClientOrder> generalClientsOrders) throws NotFoundException {
        if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exists");
        for (GeneralClientOrder generalClientOrder : generalClientsOrders) {
            if (!clientRepository.existsById(generalClientOrder.getClientId())) throw new NotFoundException("Client does not exists");
        }

        generalClientOrderRepository.saveAll(generalClientsOrders);

    }


    /**
     * modify the general delivery order for customers
     *
     * @param tourId the id of the tour
     * @param generalClientsOrders the order
     */
    @Transactional
    public void modifyTourOrder(int  tourId, List<GeneralClientOrder> generalClientsOrders) throws NotFoundException {
        if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exists");
        for (GeneralClientOrder generalClientOrder : generalClientsOrders) {
            if (!clientRepository.existsById(generalClientOrder.getClientId())) throw new NotFoundException("Client does not exists");
        }
        generalClientOrderRepository.deleteAll(generalClientOrderRepository.findAllByTourId(tourId));
        generalClientOrderRepository.saveAll(generalClientsOrders);

    }


    /**
     * read tour order
     *
     * @param tourId the id of the tour
     * @return  tour order
     */
    @Transactional
    public List<GeneralClientOrder> readTourOrder(int tourId) throws NotFoundException {
        if (!tourRepository.existsById(tourId)) throw new NotFoundException("Tour does not exists");
        return generalClientOrderRepository.findAllByTourId(tourId);
    }

}
