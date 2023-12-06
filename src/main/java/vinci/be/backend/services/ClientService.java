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
     * Reads all videos in repository
     *
     * @return all videos
     */
    @Transactional
    public List<Client> readAll() {
        return clientRepository.findAll();
    }


    @Transactional
    public Client readOne(int clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }


    @Transactional
    public boolean createOne(Client client) {
        if (clientRepository.existsById(client.getClient_id())) return false;
        clientRepository.save(client);
        return true;
    }

    @Transactional
    public boolean updateOne(Client client) {
        if (!clientRepository.existsById(client.getClient_id())) return false;
        clientRepository.save(client);
        return true;
    }
}
