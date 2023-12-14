package vinci.be.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "tours_executions")
@Table(name="tours_executions")
public class TourExecution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="tour_execution_id", nullable=false)
  private int id;

  @Column(name="state", nullable=false)
  private String state;

  @Column(name="delivery_person")
  private String deliveryPerson;

  @Column(name="vehicle_id")
  private int vehicleId;

  @Column(name="tour_id", nullable=false)
  private int tourId;

  @Column(name="execution_date", nullable=false)
  private LocalDate executionDate;

  public boolean invalid(){
    return (!state.equals(TourState.COMMENCEE.getLabel()) && !state.equals(TourState.PREVUE.getLabel()) && !state.equals(TourState.TERMINEE.getLabel()));
  }
}
