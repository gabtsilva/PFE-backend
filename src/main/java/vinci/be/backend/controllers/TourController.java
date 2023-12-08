package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.models.Tour;
import vinci.be.backend.services.TourService;


import java.util.ArrayList;
import java.util.List;

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
            return new ResponseEntity<>(tour, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping("/tour")
    public ResponseEntity<Void> createOne(@RequestBody Tour tour) {
        if (tour.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = tourService.createOne(tour);
        if (created) {
            return new ResponseEntity<>(HttpStatus.OK);
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

}
