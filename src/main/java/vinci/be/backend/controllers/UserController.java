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


    @GetMapping("/user/{email}")
    public ResponseEntity<User> readOne(@PathVariable String email) {
        if (email == null || email.isBlank() || email.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userService.readOne(email);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/user/delivery")
    public ResponseEntity<List<User>> readAllDeliveryMen() {
        ArrayList<User> deliveryMen = (ArrayList<User>) userService.getDeliveryMen();
        return new ResponseEntity<>(deliveryMen, HttpStatus.OK);

    }

    @PostMapping("/user")
    public ResponseEntity<Void> createOne(@RequestBody User user) {
        if (user.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = userService.createOne(user);
        if (created) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }




    @PutMapping("/user/{email}")
    public ResponseEntity<Void> updateOne(@PathVariable String email, @RequestBody User user) {
        if (!email.equals(user.getEmail()) )return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (user.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = userService.updateOne(user);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
