package vinci.be.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinci.be.backend.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
