package vinci.be.backend.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "texte")
@Table(name = "texte")
public class TextHelloWorld {
    @Id
    private Integer id;

    @Column(name = "text")
    private String text;
}
