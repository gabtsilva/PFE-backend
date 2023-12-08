package vinci.be.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.Order;
import vinci.be.backend.models.Tour;
import vinci.be.backend.services.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;

    }
    @GetMapping("/order")
    public ResponseEntity<List<Order>> readAll() {
        ArrayList<Order> orders = (ArrayList<Order>) orderService.readAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }


    @GetMapping("/order/{clientId}")
    public ResponseEntity<Order> readOne(@PathVariable int clientId) {
        if (clientId<=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Order order;
        try {
            order = orderService.readOne(clientId);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }


    @PostMapping("/order/{clientId}")
    public ResponseEntity<Void> createOne(@PathVariable int clientId) {
        try{
            orderService.createOne(clientId);
        }catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch(BusinessException be) {
            be.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/order/{clientId}/addArticle/{articleId}/{quantity}")
    public ResponseEntity<Void> addArticle(@PathVariable int clientId, @PathVariable int articleId, @PathVariable int quantity ) {
        if (quantity <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            orderService.addArticle(clientId, quantity, articleId);
        }catch (BusinessException businessException) {
            businessException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/order/{clientId}/removeArticle/{articleId}/{quantity}")
    public ResponseEntity<Void> removeArticle(@PathVariable int clientId, @PathVariable int articleId, @PathVariable int quantity ) {
        try {
            orderService.removeArticle(clientId, quantity, articleId);
        }catch (BusinessException businessException) {
            businessException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /*Correspond à une modification ponctuelle */
    @PostMapping("/order/{clientId}/modify/{articleId}/{quantity}")
    public ResponseEntity<Void> updateOne(@PathVariable int clientId, @PathVariable int articleId, @PathVariable int quantity ) {
        try {
            orderService.modify(clientId, quantity, articleId);
        }catch (BusinessException businessException) {
            businessException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
