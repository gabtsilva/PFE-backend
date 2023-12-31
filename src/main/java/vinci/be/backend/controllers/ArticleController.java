package vinci.be.backend.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.be.backend.models.Article;
import vinci.be.backend.services.ArticleService;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/article")
    public ResponseEntity<List<Article>> readAll() {
        ArrayList<Article> articles = (ArrayList<Article>) articleService.readAll();
        return new ResponseEntity<>(articles, HttpStatus.OK);

    }


    @GetMapping("/article/{articleId}")
    public ResponseEntity<Article> readOne(@PathVariable int articleId) {
        if (articleId <= 0 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Article article = articleService.readOne(articleId);
        if (article != null) {
            return new ResponseEntity<>(article, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @PostMapping("/article")
    public ResponseEntity<Article> createOne(@RequestBody Article article) {
        if (article.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean created  = articleService.createOne(article);
        if (created) {
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);

    }


    @PutMapping("/article/{articleId}")
    public ResponseEntity<Void> updateOne(@PathVariable int articleId, @RequestBody Article article) {
        if (articleId <=0 || article.getId() != articleId) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (article.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        boolean found  = articleService.updateOne(article);
        if (found) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
