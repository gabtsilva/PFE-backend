package vinci.be.backend.services;


import org.springframework.stereotype.Service;
import vinci.be.backend.models.Tour;
import vinci.be.backend.repositories.TourRepository;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
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

}
