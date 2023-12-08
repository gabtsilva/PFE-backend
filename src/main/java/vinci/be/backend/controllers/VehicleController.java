package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.models.Vehicle;
import vinci.be.backend.services.VehicleService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    @GetMapping("/vehicle")
    public ResponseEntity<List<Vehicle>> readAll() {
        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) vehicleService.readAll();
        return new ResponseEntity<>(vehicles, HttpStatus.OK);

    }


    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Vehicle> readOne(@PathVariable int vehicleId) {
        if (vehicleId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Vehicle vehicle = vehicleService.readOne(vehicleId);
        if (vehicle != null) {
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping("/vehicle")
    public ResponseEntity<Void> createOne(@RequestBody Vehicle vehicle) {
        if (vehicle.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = vehicleService.createOne(vehicle);
        if (created) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Void> updateOne(@PathVariable int vehicleId, @RequestBody Vehicle vehicle) {
        if (vehicleId <= 0 || vehicleId != vehicle.getId()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (vehicle.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = vehicleService.updateOne(vehicle);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
