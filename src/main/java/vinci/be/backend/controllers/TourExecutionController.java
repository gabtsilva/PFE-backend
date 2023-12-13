package vinci.be.backend.controllers;

import java.time.LocalDate;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.*;
import vinci.be.backend.services.TourExecutionService;

import java.util.ArrayList;
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

  @GetMapping("/tour/tourExecution")
  public ResponseEntity<List<TourExecution>> getAllTourExecutionForToday(){
    List<TourExecution> tourExecutionList;
    LocalDate executionDate = LocalDate.now();
    tourExecutionList = tourExecutionService.getTourByidDeliveryPersonForDate(null,executionDate);
    return new ResponseEntity<>(tourExecutionList,HttpStatus.OK);
  }

  @PostMapping("/tour/{tourId}/tourExecution")
  public ResponseEntity<TourExecution> createOne(@PathVariable int tourId, @RequestBody TourExecution tourExecution) {
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

    return new ResponseEntity<>(tourExecution, HttpStatus.CREATED);

  }

  @PatchMapping("/tour/{tourExecutionId}/tourExecution/deliveryPerson")
  public ResponseEntity<Void> updateDeliveryPerson(@PathVariable int tourExecutionId, @RequestBody User deliveryPerson){

    try {
      tourExecutionService.updatedeliveryPersonExecution(tourExecutionId, deliveryPerson.getEmail());
    } catch (NotFoundException notFoundException) {
      System.err.println(notFoundException.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/tour/{tourExecutionId}/tourExecution/van")
  public ResponseEntity<Void> updateVan(@PathVariable int tourExecutionId, @RequestBody Vehicle van){
    try {
      tourExecutionService.updateVanExecution(tourExecutionId, van.getId());
    } catch (NotFoundException notFoundException) {
      System.err.println(notFoundException.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }


  @PostMapping("/tour/{tourExecutionId}/tourExecution/begin")
  public ResponseEntity<String> beginTour(@PathVariable int tourExecutionId) throws NotFoundException {
    try {
      tourExecutionService.updateState(tourExecutionId,"commencée");
    }catch (Exception e){
      return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/tour/{tourExecutionId}/tourExecution/end")
  public ResponseEntity<String> endTour(@PathVariable int tourExecutionId) throws NotFoundException {
    try {
      tourExecutionService.updateState(tourExecutionId, "finie");
    }catch (Exception e){
      return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/tour/{tourExecutionId}/addSurplus")
  public ResponseEntity<Surplus> addSurplus(@PathVariable int tourExecutionId, @RequestBody Surplus surplus) throws NotFoundException {
    if (tourExecutionId != surplus.getTourExecutionId()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    if (surplus.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    try {
      tourExecutionService.addSurplus(tourExecutionId, surplus);
    }catch (NotFoundException nfe) {
      System.err.println(nfe.getMessage());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(surplus, HttpStatus.OK);
  }

  @GetMapping("/tour/{tourExecutionId}/tourExecution/allArticles")
public ResponseEntity<List<AllArticlesTourExecution>> getAllArticles(@PathVariable int tourExecutionId){
    try {
      List<AllArticlesTourExecution> allArticles = tourExecutionService.getAllArticles(tourExecutionId);
      return new ResponseEntity<>(allArticles,HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    @GetMapping("/tour/{tourExecutionId}/tourExecution/allArticles/client/{clientId}")
    public ResponseEntity<List<ArticlesCommande>> getAllArticlesByClient(@PathVariable int tourExecutionId, @PathVariable int clientId){
    try {
      List<ArticlesCommande > allArticles = tourExecutionService.getAllArticlesByClients(tourExecutionId,clientId);
      return new ResponseEntity<>(allArticles,HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tour/{tourExecutionId}/tourExecution/allClients")
  public ResponseEntity<List<Client>> getAllClients(@PathVariable int tourExecutionId){
    try {
      List<Client> allClients = tourExecutionService.getAllClients(tourExecutionId);
      return new ResponseEntity<>(allClients,HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/tour/{tourExecutionId}/tourExecution/distributeArticle/client/{clientId}")
  public ResponseEntity<Void> distributeArticle(@PathVariable int tourExecutionId, @PathVariable int clientId, @RequestBody List<ArticlesCommande> articlesCommandeList ){
    for (ArticlesCommande ac:articlesCommandeList) {
      if (ac.invalid()){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    try {
      tourExecutionService.updateComandeClientTourExecuution(tourExecutionId,clientId,articlesCommandeList);
      return new ResponseEntity<>(HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @GetMapping("/tourExecution/{tourExecutionId}/getClientDeliveryOrder")
  public ResponseEntity<List<ExecutionClientOrder>> getClientDeliveryOrder(@PathVariable int tourExecutionId){
      if (tourExecutionId < 0) return new ResponseEntity< >(HttpStatus.BAD_REQUEST);
      List<ExecutionClientOrder> executionClientOrders = new ArrayList<>();
      try {
         executionClientOrders =  tourExecutionService.readTourExecutionOrder(tourExecutionId);
      } catch (NotFoundException e) {
        System.err.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(executionClientOrders, HttpStatus.OK);
  }

  @GetMapping("/tourExecution/today/deliveryPerson/{idDeliveryPerson}")
  public ResponseEntity<List<TourExecution>>  getTourExecDeliverPersonForToday(@PathVariable String idDeliveryPerson){
    List<TourExecution> result ;
    LocalDate executionDate = LocalDate.now();
    try {
      result = tourExecutionService.getTourByidDeliveryPersonForDate(idDeliveryPerson,executionDate);
    }catch (Exception e){
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(result,HttpStatus.OK);
  }

}
