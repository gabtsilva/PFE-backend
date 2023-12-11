package vinci.be.backend.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vinci.be.backend.repositories.OrderLineIdentifier;
import vinci.be.backend.repositories.SurplusIdentifier;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "surplus")
@Table(name="surplus")
@IdClass(SurplusIdentifier.class)
public class Surplus {


    @Id
    @Column(name="article_id", nullable=false)
    private int articleId;

    @Id
    @Column(name="tour_id", nullable=false)
    private int tourId;

    @Column(name="percentage", nullable=false)
    private int percentage;


    public boolean invalid() {
        return percentage < 0 ;
    }
}
