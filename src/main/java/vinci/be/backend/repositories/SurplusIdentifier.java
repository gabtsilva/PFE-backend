package vinci.be.backend.repositories;

import java.io.Serializable;
import java.util.Objects;

public class SurplusIdentifier implements Serializable {

    private int tourId;
    private int articleId;


    public SurplusIdentifier() {
    }

    public SurplusIdentifier(int articleId, int tourId) {
        this.articleId = articleId;
        this.tourId = tourId;
    }

    // Getters, setters

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getOrderId() {
        return tourId;
    }

    public void setOrderId(int orderId) {
        this.tourId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurplusIdentifier that = (SurplusIdentifier) o;
        return articleId == that.articleId && tourId == that.tourId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, tourId);
    }
}
