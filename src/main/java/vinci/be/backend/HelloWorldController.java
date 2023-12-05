package vinci.be.backend;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
