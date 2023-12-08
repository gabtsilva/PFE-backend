package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.models.User;
import vinci.be.backend.models.UserWithPassword;
import vinci.be.backend.services.UserService;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public ResponseEntity<List<User>> readAll() {
        ArrayList<User> users = (ArrayList<User>) userService.readAll();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }


    @GetMapping("/user/{userMail}")
    public ResponseEntity<User> readOne(@PathVariable String userMail) {
        if (userMail == null || userMail.isBlank() || userMail.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userService.readOne(userMail);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/user/delivery}")
    public ResponseEntity<List<User>> readAllDeliveryMen() {
        ArrayList<User> deliveryMen = (ArrayList<User>) userService.getDeliveryMen();
        return new ResponseEntity<>(deliveryMen, HttpStatus.OK);

    }

    @PostMapping("/user")
    public ResponseEntity<Void> createOne(@RequestBody UserWithPassword userWithPassword) {
        if (userWithPassword.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = userService.createOne(userWithPassword);
        if (created) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/user/{userMail}")
    public ResponseEntity<Void> updateOne(@PathVariable String userMail, @RequestBody User user) {
        if (userMail == null || userMail.isBlank() || userMail.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!userMail.equals(user.getMail()) )return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (user.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = userService.updateOne(user);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
