package vinci.be.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.TourExecution;
import vinci.be.backend.models.TourState;
import vinci.be.backend.models.User;
import vinci.be.backend.models.Vehicle;
import vinci.be.backend.services.TourExecutionService;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
public class TourExecutionController {

  private final TourExecutionService tourExecutionService;

  public TourExecutionController(TourExecutionService tourExecutionService) {
    this.tourExecutionService = tourExecutionService;
  }

  @GetMapping("/tour/{tourId}/{state}")
  public ResponseEntity<List<TourExecution>> readExecutionByStateForATour(@PathVariable int tourId, @PathVariable String state) {
    if (!state.equals(TourState.PREVUE.getLabel()) && !state.equals(TourState.COMMENCEE.getLabel()) && !state.equals(TourState.TERMINEE.getLabel()))
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    ArrayList<TourExecution> toursExeuction;

    try{
      toursExeuction = (ArrayList<TourExecution>) tourExecutionService.readExecutionByStateForATour(tourId, state);
    } catch (NotFoundException nfe) {
      System.err.println(nfe.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(toursExeuction, HttpStatus.OK);

  }

  @GetMapping("/tour/{tourId}/user/{userMail}/state/{state}")
  public ResponseEntity<List<TourExecution>> readPlannedExecutionByDeliveryManForATour(@PathVariable int tourId, @PathVariable String userMail, @PathVariable String state) {
    ArrayList<TourExecution> toursExeuction;

    try{
      toursExeuction = (ArrayList<TourExecution>) tourExecutionService.readPlannedExecutionByDeliveryManForATour(tourId, userMail, state);
    } catch (NotFoundException nfe) {
      System.err.println(nfe.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (BusinessException be) {
      System.err.println(be.getMessage());
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(toursExeuction, HttpStatus.OK);

  }

  @PostMapping("/tour/{tourId}/tourExecution")
  public ResponseEntity<Void> createOne(@PathVariable int tourId, @RequestBody TourExecution tourExecution) {
    //tourExecution.setExecutionDate(tourExecution.getExecutionDate());
    tourExecution.invalid();
    if (tourExecution.invalid()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    try {
      tourExecutionService.createOneExecution(tourId, tourExecution);
    } catch (NotFoundException notFoundException) {
      System.err.println(notFoundException.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);

  }

  @PatchMapping("/tour/{tourId}/tourExecution/deliveryPerson")
  public ResponseEntity<Void> updateDeliveryPerson(@PathVariable int tourId, @RequestBody User deliveryPerson){

    try {
      tourExecutionService.updatedeliveryPersonExecution(tourId, deliveryPerson.getEmail());
    } catch (NotFoundException notFoundException) {
      System.err.println(notFoundException.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/tour/{tourId}/tourExecution/van")
  public ResponseEntity<Void> updateVan(@PathVariable int tourId, @RequestBody Vehicle van){
    try {
      tourExecutionService.updateVanExecution(tourId, van.getId());
    } catch (NotFoundException notFoundException) {
      System.err.println(notFoundException.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }


  }
