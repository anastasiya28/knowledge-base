package application.service.interfaces;


import application.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();

    Tag findById(Integer id);

    Tag findByName(String name);

    List<Tag> findAllByArticlesId(Integer id);

    Tag add(Tag tag);

    Tag update(Tag tag);

    void delete(Integer id);
}
