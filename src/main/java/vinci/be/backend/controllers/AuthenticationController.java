package vinci.be.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.models.UnsafeCredentials;
import vinci.be.backend.services.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> connect(@RequestBody UnsafeCredentials credentials) {
        if (credentials.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String jwtToken =  service.connect(credentials);
        if (jwtToken == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/auth/verify")
    public ResponseEntity<String> verify(@RequestBody String token) {
        String pseudo = service.verify(token);

        if (pseudo == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(pseudo, HttpStatus.OK);
    }


//    @PostMapping("/auth/{mail}")
//    public ResponseEntity<Void> createOne(@PathVariable String mail, @RequestBody UnsafeCredentials credentials) {
//        if (!Objects.equals(credentials.getMail(), mail)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        if (credentials.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        boolean created = service.createOne(credentials);
//
//        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
//        else return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    /*
    @PutMapping("/auth/{pseudo}")
    public ResponseEntity<Void> updateOne(@PathVariable String pseudo, @RequestBody UnsafeCredentials credentials) {
        if (!Objects.equals(credentials.getPseudo(), pseudo)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (credentials.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean found = service.updateOne(credentials);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/auth/{pseudo}")
    public ResponseEntity<Void> deleteCredentials(@PathVariable String pseudo) {
        boolean found = service.deleteOne(pseudo);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

 */

}