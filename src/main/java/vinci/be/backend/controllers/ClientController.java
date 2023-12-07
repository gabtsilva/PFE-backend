package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Client client = clientService.readOne(clientId);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/client")
    public ResponseEntity<Void> createOne(@RequestBody Client client) {
        if (client.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = clientService.createOne(client);
        if (created) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/client/{clientId}")
    public ResponseEntity<Void> updateOne(@PathVariable int clientId, @RequestBody Client client) {
        if (clientId != client.getClient_id()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (client.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = clientService.updateOne(client);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
