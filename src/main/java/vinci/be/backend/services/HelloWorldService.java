package vinci.be.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vinci.be.backend.repositories.HelloWorldRepository;
import vinci.be.backend.models.TextHelloWorld;

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
        return repository.findAll();
    }



}
