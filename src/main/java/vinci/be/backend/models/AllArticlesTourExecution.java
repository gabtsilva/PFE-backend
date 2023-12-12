package vinci.be.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllArticlesTourExecution {


  private int id;

  private String name;

  private double planned_quantity;

  private double total_with_surplus;

  public boolean invalid() {
    return name == null || name.isBlank() || name.isEmpty() ;
  }

}
