package vinci.be.backend.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientDelivered {

  private String name;

  private boolean delivred;
  public boolean getDelivered() {
    return delivred;
  }


}
