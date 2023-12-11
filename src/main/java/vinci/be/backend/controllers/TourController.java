package vinci.be.backend.controllers;


import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.GeneralClientOrder;
import vinci.be.backend.models.Tour;
import vinci.be.backend.services.TourService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
public class TourController {
    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }


    @GetMapping("/tour")
    public ResponseEntity<List<Tour>> readAll() {
        ArrayList<Tour> tours = (ArrayList<Tour>) tourService.readAll();
        return new ResponseEntity<>(tours, HttpStatus.OK);

    }


    @GetMapping("/tour/{tourId}")
    public ResponseEntity<Tour> readOne(@PathVariable int tourId) {
        if (tourId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Tour tour = tourService.readOne(tourId);
        if (tour != null) {
            return new ResponseEntity<>(tour, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping("/tour")
    public ResponseEntity<Tour> createOne(@RequestBody Tour tour) {
        if (tour.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Tour newTour  = tourService.createOne(tour);
        if (newTour != null) {
            return new ResponseEntity<>(newTour, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/tour/{tourId}")
    public ResponseEntity<Void> updateOne(@PathVariable int tourId, @RequestBody Tour tour) {
        if (tourId <= 0 ||tour.getId() != tourId) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (tour.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = tourService.updateOne(tour);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/tour/{tourId}/getTourOrder")
    public ResponseEntity<List<GeneralClientOrder>> readTourOrder(@PathVariable int tourId) {
        if (tourId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ArrayList<GeneralClientOrder> generalClientOrders;
        try {
            generalClientOrders = (ArrayList<GeneralClientOrder>) tourService.readTourOrder(tourId);
        } catch (NotFoundException nfe) {
            System.err.println(nfe.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(generalClientOrders, HttpStatus.OK);

    }


    @PostMapping("/tour/{tourId}/createTourOrder")
    public ResponseEntity<List<GeneralClientOrder>> createTourOrder(@PathVariable int tourId, @RequestBody List<GeneralClientOrder> generalClientsOrders) {
        Set<Integer> uniqueOrders = new HashSet<>();
        Set<Integer> uniqueTour = new HashSet<>();
        uniqueTour.add(tourId);
        for (GeneralClientOrder generalClientOrder : generalClientsOrders) {
            if (generalClientOrder.invalid() || tourId != generalClientOrder.getTourId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            //ce numéro d'ordre est déjà présent dans la liste
            if (!uniqueOrders.add(generalClientOrder.getOrder())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //plus de un tourid dans l'ordre
            if (uniqueTour.add(generalClientOrder.getTourId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        try {
            tourService.createOrder(tourId, generalClientsOrders);
        }catch (NotFoundException nfe) {
            System.err.println(nfe.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(ConflictException cfe) {
            System.err.println(cfe.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(generalClientsOrders, HttpStatus.OK);
    }



    @PutMapping("/tour/{tourId}/modifyTourOrder")
    public ResponseEntity<List<GeneralClientOrder>> modifyTourOrder(@PathVariable int tourId, @RequestBody List<GeneralClientOrder> generalClientsOrders) {
        Set<Integer> uniqueOrders = new HashSet<>();
        Set<Integer> uniqueTour = new HashSet<>();
        uniqueTour.add(tourId);
        for (GeneralClientOrder generalClientOrder : generalClientsOrders) {
            if (generalClientOrder.invalid() || tourId != generalClientOrder.getTourId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            //ce numéro d'ordre est déjà présent dans la liste
            if (!uniqueOrders.add(generalClientOrder.getOrder())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //plus de un tourid dans l'ordre
            if (uniqueTour.add(generalClientOrder.getTourId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        try {
            tourService.modifyTourOrder(tourId, generalClientsOrders);
        }catch (NotFoundException nfe) {
            System.err.println(nfe.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(generalClientsOrders, HttpStatus.OK);
    }

}
