package vinci.be.backend.repositories;

import java.io.Serializable;
import java.util.Objects;

public class OrderLineIdentifier implements Serializable {

    private int articleId;
    private int orderId;


    public OrderLineIdentifier() {
    }

    public OrderLineIdentifier(int articleId, int orderId) {
        this.articleId = articleId;
        this.orderId = orderId;
    }

    // Getters, setters

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Méthodes equals() et hashCode() générées automatiquement par votre IDE
    // Assurez-vous qu'elles comparent tous les champs de la clé composée

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineIdentifier that = (OrderLineIdentifier) o;
        return articleId == that.articleId && orderId == that.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, orderId);
    }
}
