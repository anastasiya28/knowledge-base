package application.controller;

import application.service.interfaces.ArticleService;
import application.service.interfaces.SectionService;
import application.model.Article;
import application.model.User;
import application.model.UserRole;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@Transactional
public class ArticleController {
    private static final Logger logger = Logger.getLogger(ArticleController.class);
    @Autowired
    ArticleService articleService;

    @Autowired
    SectionService sectionService;

    @RequestMapping(value = "/api/article/list", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findArticlesAll() {
        List<Article> articles = articleService.findAll();
        String json;
        if (articles.size() == 0) return "[]";
        json = "[";
        for (Article article : articles) {
            String item = "{";
            item += "\"id\":" + article.getId() + ",\"name\":\"" + article.getName() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    @RequestMapping(value = "/api/article/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findArticleById(@PathVariable("id") int id, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Article article = articleService.findById(id);
        if (article == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());//404
            return "{\"errorMessage\":\"Статья не найдена на сервере.\"}";
        }
        try {
            json.put("id", id);
            json.put("filename", article.getName());
            json.put("title", article.getTitle());
            json.put("sectionId", article.getSection().getId());

            String path = "D:/Projects/IdeaProjects/simple-web-application/articles/" + article.getName();
            json.put("content", new String(Files.readAllBytes(Paths.get(path))));
        } catch (JSONException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());//500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        } catch (IOException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value()); //404
            return "{\"errorMessage\":\"Статья \"" + article.getTitle() + "\" не найдена на сервере.\"}";
        }
        return json.toString();
    }

    @RequestMapping(value = "/api/article/tag/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findArticlesByTag(@PathVariable("id") int id) {
        List<Article> articles = articleService.findAllByTagsId(id);
        String json = "";
        if (articles.size() == 0) return "[]";
        json = "[";
        for (Article article : articles) {
            String item = "{";
            item += "\"id\":" + article.getId() + ",\"name\":\"" + article.getTitle() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    @RequestMapping(value = "/api/article/new", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String createArticle(@RequestParam("sectionId") Integer sectionId,
                                @RequestParam("articleTitle") String title,
                                @RequestParam("articleFaleName") String fileName,
                                @RequestParam("articleContent") String content,
                                HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        User securityUser = (User) request.getAttribute("securityUser");
        if (securityUser == null) {
            logger.error("Не установлен контектст безопасности. securityUser == null !!!");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }

        if (!securityUser.getRole().getName().equals(UserRole.ROLE_ADMIN.toString())) {
            response.setStatus(HttpStatus.FORBIDDEN.value()); //403
            return "{\"errorMessage\":\"Доступ запрещен. Отсутствуют права на добавление статьи.\"}";
        }

        if (title == null || title.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Заголовок статьи отсутсвует!\"}";
        }

        if (fileName == null || fileName.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Не указано имя файла для хранения статьи!\"}";
        }

        //Проверка корректности заданного пользователем имени файла для хранения статьи;
        if (checkFileName(fileName)) {
            logger.info("The file name is set correctly!");
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Неверно задано имя файла! При именовании файла можно использовать латинские " +
                    "буквы (строчные и заглавные), цифры, точку и нижнее подчеркивание. " +
                    "Общая длина имени файла должна быть не менее одного символа и не более 50 символов.\"}";
        }

        content = URLDecoder.decode(content, "UTF-8");
        if (content.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Статья не может быть пустой!\"}";
        }

        fileName = fileName + ".md";
        Path path = Paths.get("D:/Projects/IdeaProjects/simple-web-application/articles/" + fileName);
        if (Files.exists(path)) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());//422
            return "{\"errorMessage\":\"Файл с таким именем уже существует!\"}";
        }

        Article article = new Article();
        article.setTitle(title);
        article.setName(fileName);
        article.setSection(sectionService.findById(sectionId));

        Files.write(path, content.getBytes("UTF-8"));

        try {
            articleService.add(article);
        }catch (IllegalArgumentException e){
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());//422
            return "{\"errorMessage\":\"Такая статья в системе уже есть!\"}";
        }

        return "{\"result\":\"SUCCESS\"}";
    }

    @RequestMapping(value = "/api/article/{id}/update", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String updateArticleById(@RequestParam("id") Integer id,
                                    @RequestParam("content") String content,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User securityUser = (User) request.getAttribute("securityUser");
        if (securityUser == null) {
            logger.error("Не установлен контектст безопасности. securityUser == null !!!");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }

        if (!securityUser.getRole().getName().equals(UserRole.ROLE_ADMIN.toString())) {
            response.setStatus(HttpStatus.FORBIDDEN.value()); //403
            return "{\"errorMessage\":\"Доступ запрещен. Отсутствуют права на редактирование статьи.\"}";
        }

        if (id == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"id статьи отсутсвует!\"}";
        }

        if (content == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Содержимое статьи отсутствует!\"}";
        }

        content = URLDecoder.decode(content, "UTF-8");
        if (content.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Статья не может быть пустой!\"}";
        }

        Article article = articleService.findById(id);
        Path path = Paths.get("D:/Projects/IdeaProjects/simple-web-application/articles/" + article.getName());
        Files.write(path, content.getBytes("UTF-8"));
        return "{\"result\":\"SUCCESS\"}";
    }

    private boolean checkFileName(String fileName) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._]{1,50}$");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.matches();
    }

    @RequestMapping(value = "/api/article/{id}/delete", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String deleteArticleById(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        User securityUser = (User) request.getAttribute("securityUser");
        if (securityUser == null) {
            logger.error("Не установлен контектст безопасности. securityUser == null !!!");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }

        if (!securityUser.getRole().getName().equals(UserRole.ROLE_ADMIN.toString())) {
            response.setStatus(HttpStatus.FORBIDDEN.value()); //403
            return "{\"errorMessage\":\"Доступ запрещен. Отсутствуют права на редактирование статьи.\"}";
        }

        if (id == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"id статьи отсутсвует!\"}";
        }

        articleService.delete(id);

        return "{\"result\":\"SUCCESS\"}";
    }
}