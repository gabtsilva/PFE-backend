package vinci.be.backend;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    private final HelloWorldRepository repository;
    public HelloWorldService(HelloWorldRepository repository) {
        this.repository = repository;
    }

    /**
     * Reads all videos in repository
     *
     * @return all videos
     */
    @Transactional
    public Iterable<TextHelloWorld> readAll() {
        System.out.println("élément(s) : " + repository.findAll());
        return repository.findAll();
    }



}
