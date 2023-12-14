package vinci.be.backend.models;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientDelivredQty {



  private int clientId ;
  private String clientName ;
  private List<ArticlesCommande> articles;

}
