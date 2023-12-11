package vinci.be.backend.repositories;

import java.io.Serializable;
import java.util.Objects;

public class SurplusIdentifier implements Serializable {

    private int tourExecutionId;
    private int articleId;


    public SurplusIdentifier() {
    }

    public SurplusIdentifier(int articleId, int tourExecutionId) {
        this.articleId = articleId;
        this.tourExecutionId = tourExecutionId;
    }

    // Getters, setters

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getOrderId() {
        return tourExecutionId;
    }

    public void setOrderId(int orderId) {
        this.tourExecutionId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurplusIdentifier that = (SurplusIdentifier) o;
        return articleId == that.articleId && tourExecutionId == that.tourExecutionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, tourExecutionId);
    }
}
