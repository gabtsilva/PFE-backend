package vinci.be.backend.services;

import org.springframework.stereotype.Service;
import vinci.be.backend.exceptions.BusinessException;
import vinci.be.backend.exceptions.NotFoundException;
import vinci.be.backend.models.Order;
import vinci.be.backend.models.OrderLine;
import vinci.be.backend.repositories.*;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    private final ArticleRepository articleRepository;

    private final OrderLineRepository orderLineRepository;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, ArticleRepository articleRepository, OrderLineRepository orderLineRepository){
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.orderLineRepository = orderLineRepository;
    }

    /**
     * Reads all orders in repository
     *
     * @return all orders
     */
    public List<Order> readAll() {
        return orderRepository.findAll();
    }


    /**
     * Retrieves a specific order from the repository based on the provided client ID.
     *
     * @param clientId The unique identifier of the client .
     * @return The order of the client or null if not found.
     */
    public Order readOne(int clientId) throws NotFoundException {
        verifyIfClientExists(clientId);

        Order order = orderRepository.findByClientId(clientId);

        if (order == null) throw new NotFoundException("Le client n'a pas encore de commande");

        return order;
    }


    /**
     * Retrieves all articles from an user order.
     *
     * @param clientId The unique identifier of the client .
     * @return all articles.
     */
    public List<OrderLine> readAllArticleFromAnOrder(int clientId) throws NotFoundException {
        verifyIfClientExists(clientId);

        Order order = orderRepository.findByClientId(clientId);

        if (order == null) throw new NotFoundException("Le client n'a pas encore de commande");

        return orderLineRepository.findByOrderId(order.getId());
    }

    /**
     * Creates a new order in the repository if the client does not already have an order.
     *
     * @param clientId The unique identifier of the client to retrieve its order
     */
    public Order createOne(int clientId) throws NotFoundException, BusinessException {
        verifyIfClientExists(clientId);
        Order order = orderRepository.findByClientId(clientId);
        if (order != null) throw new BusinessException("Le client a déjà une commande");
        Order newOrder = new Order();
        newOrder.setClientId(clientId);
        orderRepository.save(newOrder);
        return newOrder;
    }



    /**
     * add articles to an order.
     *
     * @param clientId The unique identifier of the client
     * @param articleId The unique identifier of the article
     * @param quantity The quantity to add
     */
    public void addArticle(int clientId, double quantity, int articleId) throws NotFoundException, BusinessException {
        verifyIfClientExists(clientId);
        verifyIfArticleExists(articleId);

        Order order = orderRepository.findByClientId(clientId);
        if (order == null) {
            throw new NotFoundException("Le client n'a pas de commande à laquelle ajouter des articles");
        }


        OrderLineIdentifier pk = new OrderLineIdentifier(articleId, order.getId());
        OrderLine orderLine = orderLineRepository.findById(pk).orElse(null);

        //il existe déjà une ligne de commande pour cet article
        if(orderLine != null) {
            orderLine.setPlannedQuantity(orderLine.getPlannedQuantity() + quantity);
            orderLineRepository.save(orderLine);
        }else{
            orderLine = new OrderLine(quantity, 0,0 ,articleId, clientId);
            orderLineRepository.save(orderLine);
        }
    }


    /**
     * remove articles to an order.
     *
     * @param clientId The unique identifier of the client
     * @param articleId The unique identifier of the article
     * @param quantity The quantity to add
     */
    public void removeArticle(int clientId, double quantity, int articleId) throws NotFoundException, BusinessException {
        verifyIfClientExists(clientId);
        verifyIfArticleExists(articleId);

        Order order = orderRepository.findByClientId(clientId);
        if (order == null) {
            throw new NotFoundException("Le client n'a pas de commande à laquelle retirer des articles");
        }


        OrderLineIdentifier pk = new OrderLineIdentifier(articleId, order.getId());
        OrderLine orderLine = orderLineRepository.findById(pk).orElse(null);

        if(orderLine == null) {
            throw new NotFoundException("Le client n'a pas cet article dans sa commande");
        }else{
            if (quantity > orderLine.getPlannedQuantity()) {
                throw new BusinessException("Vous tentez de soustraire une quantité trop grande");
            }
            orderLine.setPlannedQuantity(orderLine.getPlannedQuantity() - quantity);
            orderLineRepository.save(orderLine);
        }
    }



    /**
     * modify temporaly an article to an order. Once the tour is over, this quantity must be deleted /*TODO
     *
     * @param clientId The unique identifier of the client
     * @param articleId The unique identifier of the article
     * @param quantity The quantity to add
     */
    public void modify(int clientId, double quantity, int articleId) throws NotFoundException, BusinessException {
        verifyIfClientExists(clientId);
        verifyIfArticleExists(articleId);

        Order order = orderRepository.findByClientId(clientId);
        if (order == null) {
            throw new NotFoundException("Le client n'a pas de commande à laquelle retirer des articles");
        }


        OrderLineIdentifier pk = new OrderLineIdentifier(articleId, order.getId());
        OrderLine orderLine = orderLineRepository.findById(pk).orElse(null);

        if(orderLine == null) {
            throw new NotFoundException("Le client n'a pas cet article dans sa commande");
        }else{
            orderLine.setChangedQuantity(orderLine.getChangedQuantity() + quantity);
            orderLineRepository.save(orderLine);
        }
    }


    /** Verify if the client exists in the repository
     *
     *
     * @param clientId the id of the client
     * .
     */
    private void verifyIfClientExists(int clientId) throws NotFoundException {
        if(!clientRepository.existsById(clientId)) throw new NotFoundException("Le client n'existe pas");
    }


    /** Verify if the article exists in the repository
     *
     *
     * @param articleId the id of the client
     * .
     */
    private void verifyIfArticleExists(int articleId) throws NotFoundException {
        if(!articleRepository.existsById(articleId)) throw new NotFoundException("L'article n'existe pas");
    }

}
