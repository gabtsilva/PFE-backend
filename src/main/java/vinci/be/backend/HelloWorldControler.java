package vinci.be.backend;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldControler {
    private final HelloWorldService service;
    public HelloWorldControler(HelloWorldService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public Iterable<TextHelloWorld> read() {
        return service.readAll();
    }

}
