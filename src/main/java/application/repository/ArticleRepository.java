package application.repository;

import application.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllBySectionId(Integer sectionId);

    List<Article> findAllByTagsId(Integer tagId);
}

