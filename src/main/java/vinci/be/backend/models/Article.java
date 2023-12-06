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
@Entity(name = "articles")
@Table(name="articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int article_id;

    @Column(name="article_name", nullable=false)
    private String name;


    public boolean invalid() {
        return name.isBlank() || name.isEmpty();
    }

}
