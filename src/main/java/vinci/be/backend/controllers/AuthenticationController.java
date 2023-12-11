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
        System.out.println("tried to connect");
        if (credentials.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        String jwtToken =  service.connect(credentials);
        System.out.println(jwtToken);
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
}