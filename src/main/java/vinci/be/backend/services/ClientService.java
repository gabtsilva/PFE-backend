package vinci.be.backend.services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vinci.be.backend.models.Client;
import vinci.be.backend.repositories.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Reads all clients in repository
     *
     * @return all clients
     */
    public List<Client> readAll() {
        return clientRepository.findAll();
    }


    /**
     * Retrieves a specific client from the repository based on the provided client ID.
     *
     * @param clientId The unique identifier of the client to be retrieved.
     * @return The client object corresponding to the provided client ID, or null if not found.
     */
    public Client readOne(int clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }


    /**
     * Creates a new client in the repository if the provided client ID does not already exist.
     *
     * @param client The client object to be created.
     * @return true if the client is successfully created, false if a client with the same ID already exists.
     */
    public boolean createOne(Client client) {
        if (clientRepository.existsById(client.getClient_id())) return false;
        clientRepository.save(client);
        return true;
    }

    /**
     * Updates an existing client in the repository if the provided client ID already exists.
     *
     * @param client The client object with updated information.
     * @return true if the client is successfully updated, false if the client with the provided ID does not exist.
     */
    public boolean updateOne(Client client) {
        if (!clientRepository.existsById(client.getClient_id())) return false;
        clientRepository.save(client);
        return true;
    }
}
