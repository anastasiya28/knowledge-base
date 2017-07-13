package application.service.impls;

import application.service.interfaces.ArticleService;
import application.model.Article;
import application.repository.ArticleRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);

    @Autowired
    ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Article findById(Integer id) {
        Article article = articleRepository.findOne(id);
        if (article == null) {
            logger.info("Article with id = " + id + " does not exist in the database");
        }
        return article;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Article> findAllBySectionId(Integer sectionId) {
        List<Article> articles = articleRepository.findAllBySectionId(sectionId);
        if (articles.size() == 0) {
            logger.info("There are no articles in the section with id = " + sectionId);
        }
        return articles;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Article> findAllByTagsId(Integer tagId) {
        List<Article> articles = articleRepository.findAllByTagsId(tagId);
        if (articles.size() == 0) {
            logger.info("There are no articles with tagId = " + tagId);
        }
        return articles;
    }

    @Transactional
    @Override
    public Article add(Article article) throws IllegalArgumentException{
        if (article.getId() != null) {
            throw new IllegalArgumentException("The article with id: " + article.getId() + "  already exists in the database");
        }
        validate(article);
        return articleRepository.saveAndFlush(article);
    }

    @Transactional
    @Override
    public Article update(Article article) throws IllegalArgumentException {
        if (article.getId() == null) {
            throw new IllegalArgumentException("ArticleId could not be null!");
        }
        validate(article);
        return articleRepository.saveAndFlush(article);
    }

    private void validate(Article article) throws IllegalArgumentException {
        if (article.getName() == null) {
            throw new IllegalArgumentException("ArticleName could not be null!");
        }

        if (article.getName().isEmpty()) {
            throw new IllegalArgumentException("ArticleName could not be empty!");
        }
        if (article.getTitle() == null) {
            throw new IllegalArgumentException("ArticleTitle could not be null!");
        }

        if (article.getTitle().isEmpty()) {
            throw new IllegalArgumentException("ArticleTitle could not be empty!");
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        if (articleRepository.findOne(id) == null) {
            logger.info("Article with id = " + id + " does not exist in the database");
        } else articleRepository.delete(id);
    }
}
