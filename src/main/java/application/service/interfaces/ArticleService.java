package application.service.interfaces;

import application.model.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    List<Article> findAll();

    Article findById(Integer id);

    List<Article> findAllBySectionId(Integer sectionId);

    List<Article> findAllByTagsId(Integer tagId);

    Article add(Article article);

    Article update(Article article);

    void delete(Integer id);
}
