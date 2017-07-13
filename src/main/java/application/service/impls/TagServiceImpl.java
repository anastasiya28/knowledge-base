package application.service.impls;

import application.service.interfaces.TagService;
import application.model.Tag;
import application.repository.TagRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = Logger.getLogger(TagServiceImpl.class);

    @Autowired
    TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(Integer id) {
        Tag tag = tagRepository.findOne(id);
        if (tag == null) {
            logger.info("Tag with id = " + id + " does not exist in the database");
        }
        return tag;
    }

    @Transactional(readOnly = true)
    @Override
    public Tag findByName(String name) {
        Tag tag = tagRepository.findByName(name);
        if (tag == null) {
            logger.info("Tag with name = " + name + " does not exist in the database");
        }
        return tag;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tag> findAllByArticlesId(Integer id) {
        List<Tag> tags = tagRepository.findAllByArticlesId(id);
        if (tags.size() == 0) {
            logger.info("For article with id = " + id + " no tags are specified");
        }
        return tags;
    }

    @Transactional
    @Override
    public Tag add(Tag tag) throws IllegalArgumentException {
        if (tag.getId() != null) {
            throw new IllegalArgumentException("The tag with id: " + tag.getId() + "  already exists in the database");
        }
        validate(tag);
        //Эта проверяется условие: UNIQUE, которое установлено на поле "name" в таблице "tag" в БД
        if (userExist(tag.getName())) {
            throw new IllegalArgumentException("The tag with login: " + tag.getName() + "  already exists in the database");
        }
        return tagRepository.saveAndFlush(tag);
    }

    @Transactional
    @Override
    public Tag update(Tag tag) throws IllegalArgumentException {
        if (tag.getId() == null) {
            throw new IllegalArgumentException("TagID could not be null!");
        }
        validate(tag);
        return tagRepository.saveAndFlush(tag);
    }

    private void validate(Tag tag) throws IllegalArgumentException {
        if (tag.getName() == null) {
            throw new IllegalArgumentException("ArticleName could not be null!");
        }

        if (tag.getName().isEmpty()) {
            throw new IllegalArgumentException("ArticleName could not be empty!");
        }
    }

    private boolean userExist(String name) {
        Tag tag = tagRepository.findByName(name);
        if (tag != null) {
            return true;
        }
        return false;
    }


    @Transactional
    @Override
    public void delete(Integer id) {
        if (tagRepository.findOne(id) == null) {
            logger.info("Tag with id = " + id + " does not exist in the database");
        }
        tagRepository.delete(id);
    }
}
