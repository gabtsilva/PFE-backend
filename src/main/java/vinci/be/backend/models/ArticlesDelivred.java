package vinci.be.backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticlesDelivred {

  private String articleName;

  private int articleId;

  private double qtyBase;

  private double qtyLivre;

  private double qtySurplusRestant;

}
