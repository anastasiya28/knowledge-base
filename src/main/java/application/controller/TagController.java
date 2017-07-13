package application.controller;

import application.service.interfaces.ArticleService;
import application.service.interfaces.TagService;
import application.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/api/tag/list", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findTagsAll(){
        List<Tag> tags = tagService.findAll();
        String json;
        if (tags.size() == 0) return "[]";
        json = "[";
        for (Tag tag : tags) {
            String item = "{";
            item += "\"id\":" + tag.getId() + ",\"name\":\"" + tag.getName() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    @RequestMapping(value = "/api/tag/article/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findTagsByArticle(@PathVariable("id") Integer articleId){
        List<Tag> tags = tagService.findAllByArticlesId(articleId);
        String json = "";
        if (tags.size() == 0) return "[]";
        json = "[";
        for (Tag tag : tags) {
            String item = "{";
            item += "\"id\":" + tag.getId() + ",\"name\":\"" + tag.getName() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }
}
