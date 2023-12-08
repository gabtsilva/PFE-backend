package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.Client;
import vinci.be.backend.services.ClientService;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/client")
    public ResponseEntity<List<Client>> readAll() {
        ArrayList<Client> clients = (ArrayList<Client>) clientService.readAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);

    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<Client> readOne(@PathVariable int clientId) {
        if (clientId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Client client = clientService.readOne(clientId);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/client")
    public ResponseEntity<Void> createOne(@RequestBody Client client) {
        System.out.println(client.invalid());
        if (client.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            clientService.createOne(client);
        } catch (ConflictException conflictException) {
            conflictException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        } catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/client/{clientId}")
    public ResponseEntity<Void> updateOne(@PathVariable int clientId, @RequestBody Client client) {
        if (clientId <= 0 || clientId != client.getId()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (client.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            clientService.updateOne(client);
        } catch (ConflictException conflictException) {
            conflictException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        } catch (NotFoundException notFoundException) {
            notFoundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
