package application.controller;

import application.service.interfaces.ArticleService;
import application.service.interfaces.SectionService;
import application.service.interfaces.TagService;
import application.model.Article;
import application.model.Section;
import application.model.User;
import application.model.UserRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@Transactional
public class SectionController {
    private static final Logger logger = Logger.getLogger(SectionController.class);

    @Autowired
    SectionService sectionService;

    @Autowired
    ArticleService articleService;

    /**
     * Возвращает сведение о разделе с заданным id
     *
     * @param id - идентификатор раздела
     * @return - JSON объект, содержащий id и name раздела
     */
    @RequestMapping(value = "/api/section/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findById(@PathVariable("id") int id) {
        Section section = sectionService.findById(id);
        String json = "{";
        json += "\"id\":" + section.getId() + ",\"name\":\"" + section.getName() + "\"" + ",\"parentId\":\"" + section.getParentId() + "\"";
        json += "}";
        return json;
    }

    @Autowired
    TagService tagService;

    /**
     * Возвращает массив дочерних разделов по заданному parentId родительксого раздела
     *
     * @param parentId - идентификатор родительского раздела
     * @return - JSON массив дочерних разделов
     */
    @RequestMapping(value = "/api/section/{parentId}/child_section_list", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findByParentId(@PathVariable("parentId") int parentId) {
        List<Section> sections = sectionService.findByParentId(parentId);
        String json = "";
        if (sections.size() == 0) return "[]";
        json = "[";
        for (Section s : sections) {
            String item = "{";
            item += "\"id\":" + s.getId() + ",\"name\":\"" + s.getName() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    @RequestMapping(value = "/api/section/{id}/article_list", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findArticleBySection(@PathVariable("id") int id) {
        List<Article> articles = articleService.findAllBySectionId(id);
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

    @RequestMapping(value = "/api/section/{id}/update", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String updateSection(@RequestParam("id") Integer id, @RequestParam("name") String name,
                                HttpServletRequest request, HttpServletResponse response) throws
            IllegalArgumentException, IOException, ServletException {
        User securityUser = (User) request.getAttribute("securityUser");
        if (securityUser == null) {
            logger.error("Не установлен контектст безопасности. securityUser == null !!!");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }

        if (!securityUser.getRole().getName().equals(UserRole.ROLE_ADMIN.toString())) {
            response.setStatus(HttpStatus.FORBIDDEN.value()); //403
            return "{\"errorMessage\":\"Доступ запрещен. Отсутствут права на редактирование разделов.\"}";
        }

        if (id == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"id раздела отсутсвует!\"}";
        }

        if (name == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Имя раздела отсутсвует!\"}";
        }

        name = URLDecoder.decode(name, "UTF-8");
        if (name.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Название раздела не может быть пустым!\"}";
        }

        Section section = sectionService.findById(id);
        section.setName(name);
        sectionService.update(section);
        return "{\"result\":\"SUCCESS\"}";
    }
}
