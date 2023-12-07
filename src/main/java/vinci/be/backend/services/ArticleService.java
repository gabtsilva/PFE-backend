package vinci.be.backend.services;

import org.springframework.stereotype.Service;
import vinci.be.backend.models.Article;
import vinci.be.backend.repositories.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Reads all articles in repository
     *
     * @return all videos
     */
    public List<Article> readAll() {
        return articleRepository.findAll();
    }


    /**
     * Retrieves a specific article from the repository based on the provided article ID.
     *
     * @param articleId The unique identifier of the articleId to be retrieved.
     * @return The Article object corresponding to the provided articleId ID, or null if not found.
     */
    public Article readOne(int articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }


    /**
     * Creates a new article in the repository if the provided article ID does not already exist.
     *
     * @param article The article object to be created.
     * @return true if the article is successfully created, false if a article with the same ID already exists.
     */
    public boolean createOne(Article article) {
        if (articleRepository.existsById(article.getArticle_id())) return false;
        articleRepository.save(article);
        return true;
    }

    /**
     * Updates an existing article in the repository if the provided article ID already exists.
     *
     * @param article The article object with updated information.
     * @return true if the article is successfully updated, false if the article with the provided ID does not exist.
     */
    public boolean updateOne(Article article) {
        if (!articleRepository.existsById(article.getArticle_id())) return false;
        articleRepository.save(article);
        return true;
    }


    /**
     * Deletes an existing article in the repository if the provided article ID already exists.
     *
     * @param article The article object to be deleted.
     * @return true if the article is successfully deleted, false if the article with the provided ID does not exist.
     */
    /*TODO réfléchir à la logique de suppression : si on supprime un article, il ne doit pas avoir de relations restantes*/
    //Devons-nous réellement implémenter les routes de delete ?
    public boolean deleteOne(Article article) {
        if (!articleRepository.existsById(article.getArticle_id())) return false;
        articleRepository.delete(article);
        return true;
    }


}
