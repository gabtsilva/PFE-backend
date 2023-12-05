package vinci.be.backend.controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vinci.be.backend.services.HelloWorldService;
import vinci.be.backend.models.TextHelloWorld;

@RestController
public class HelloWorldController {
    private final HelloWorldService service;
    public HelloWorldController(HelloWorldService service) {
        this.service = service;
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/hello")
    public Iterable<TextHelloWorld> read() {
        return service.readAll();
    }

}
