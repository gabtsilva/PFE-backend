package vinci.be.backend.services;


import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.ConflictException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.Client;
import vinci.be.backend.models.GeneralClientOrder;
import vinci.be.backend.repositories.ClientRepository;
import vinci.be.backend.repositories.GeneralClientOrderRepository;
import vinci.be.backend.repositories.TourRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final TourRepository tourRepository;
    private final GeneralClientOrderRepository generalClientOrderRepository;
    public ClientService(ClientRepository clientRepository, TourRepository tourRepository, GeneralClientOrderRepository generalClientOrderRepository) {
        this.clientRepository = clientRepository;
        this.tourRepository = tourRepository;
        this.generalClientOrderRepository = generalClientOrderRepository;
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
    public void createOne(Client client) throws NotFoundException, ConflictException {
        if (!tourRepository.existsById(client.getTour())) throw new NotFoundException("La tournée du client n'existe pas");
        if (clientRepository.existsById(client.getId())) throw new ConflictException("La client existe déjà");
        clientRepository.save(client);
    }

    /**
     * Updates an existing client in the repository if the provided client ID already exists.
     *
     * @param client The client object with updated information.
     * @return true if the client is successfully updated, false if the client with the provided ID does not exist.
     */
    public boolean updateOne(Client client) throws NotFoundException, ConflictException {
        if (!tourRepository.existsById(client.getTour())) throw new NotFoundException("La tournée du client n'existe pas");
        if (!clientRepository.existsById(client.getId())) throw new NotFoundException("La client n'existe pas");
        Client savedClient = clientRepository.getReferenceById(client.getId());

        //Si on change le client de tournée
        if (savedClient.getTour() != client.getTour()) {
            GeneralClientOrder clientOrder = generalClientOrderRepository.findByClientId(client.getId());
            clientOrder.setTourId(client.getTour());
            //On met l'ordre du client dans sa nouvelle tournée à la plus grande par défaut
            //si dans la tournée 1 il y a 2 clients, le client qu'on update sera à l'ordre 3
            int maxOrder =  generalClientOrderRepository.findMaxOrderByTourId(client.getTour()).getOrder();
            clientOrder.setOrder(maxOrder + 1);
            generalClientOrderRepository.save(clientOrder);

        }
        clientRepository.save(client);
        return true;
    }

    /**
     * Reads all clients by specific tour in repository
     *
     * @return all clients by specific tour
     */
    public List<Client> readAllByTour(int indexTour) {
        return clientRepository.findAllByIdTour(indexTour);
    }
}
