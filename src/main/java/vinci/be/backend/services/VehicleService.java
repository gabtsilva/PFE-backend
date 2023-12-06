package vinci.be.backend.services;


import org.springframework.stereotype.Service;
import vinci.be.backend.models.Client;
import vinci.be.backend.models.Vehicle;
import vinci.be.backend.repositories.VehicleRepository;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Reads all vehicles in repository
     *
     * @return all videos
     */
    public List<Vehicle> readAll() {
        return vehicleRepository.findAll();
    }


    /**
     * Retrieves a specific vehicle from the repository based on the provided vehicle ID.
     *
     * @param vehicleId The unique identifier of the vehicleId to be retrieved.
     * @return The Vehicle object corresponding to the provided vehicleId ID, or null if not found.
     */
    public Vehicle readOne(int vehicleId) {
        return vehicleRepository.findById(vehicleId).orElse(null);
    }


    /**
     * Creates a new vehicle in the repository if the provided vehicle ID does not already exist.
     *
     * @param vehicle The vehicle object to be created.
     * @return true if the vehicle is successfully created, false if a vehicle with the same ID already exists.
     */
    public boolean createOne(Vehicle vehicle) {
        if (vehicleRepository.existsById(vehicle.getVehicle_id())) return false;
        vehicleRepository.save(vehicle);
        return true;
    }

    /**
     * Updates an existing vehicle in the repository if the provided vehicle ID already exists.
     *
     * @param vehicle The vehicle object with updated information.
     * @return true if the vehicle is successfully updated, false if the vehicle with the provided ID does not exist.
     */
    public boolean updateOne(Vehicle vehicle) {
        if (!vehicleRepository.existsById(vehicle.getVehicle_id())) return false;
        vehicleRepository.save(vehicle);
        return true;
    }


    /**
     * Deletes an existing vehicle in the repository if the provided vehicle ID already exists.
     *
     * @param vehicle The vehicle object to be deleted.
     * @return true if the vehicle is successfully deleted, false if the vehicle with the provided ID does not exist.
     */
    public boolean deleteOne(Vehicle vehicle) {
        if (!vehicleRepository.existsById(vehicle.getVehicle_id())) return false;
        vehicleRepository.delete(vehicle);
        return true;
    }


}
