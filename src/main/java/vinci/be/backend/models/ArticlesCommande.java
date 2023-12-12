package vinci.be.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vinci.be.backend.repositories.OrderLineIdentifier;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ArticlesCommande {


    private double plannedQuantity;

    private double deliveredQuantity;

    private double changedQuantity;


    private int articleId;

    private String articleName;



}
