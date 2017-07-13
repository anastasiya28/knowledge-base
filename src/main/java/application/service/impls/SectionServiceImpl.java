package application.service.impls;

import application.service.interfaces.SectionService;
import application.model.Section;
import application.repository.SectionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {
    private static final Logger logger = Logger.getLogger(SectionServiceImpl.class);

    @Autowired
    private SectionRepository sectionRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Section findById(Integer id) {
        Section section = sectionRepository.findOne(id);
        if (section == null) {
            logger.info("Section with id = " + id + " does not exist in the database");
        }
        return section;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Section> findByParentId(Integer id) {
        List<Section> sections = sectionRepository.findByParentId(id);
        if (sections.isEmpty()) {
            logger.info("Sections with parent_id = " + id + " does not exist in the database");
        }
        return sections;
    }

    @Transactional
    @Override
    public Section add(Section section, Integer psrentId) throws IllegalArgumentException {
        if (section.getId() != null) {
            throw new IllegalArgumentException("The section with id: " + section.getId() + "  already exists in the database");
        }
        validate(section);
        if (psrentId == null) {
            section.setParentId(1);
            return sectionRepository.saveAndFlush(section);
        }
        section.setParentId(psrentId);
        return sectionRepository.saveAndFlush(section);
    }

    @Transactional
    @Override
    public Section update(Section section) throws IllegalArgumentException {
        if (section.getId() == null) {
            throw new IllegalArgumentException("SectionID could not be null!");
        }
        validate(section);
        return sectionRepository.saveAndFlush(section);
    }

    private void validate(Section section) throws IllegalArgumentException {
        if (section.getName() == null) {
            throw new IllegalArgumentException("SectionName could not be null!");
        }

        if (section.getName().isEmpty()) {
            throw new IllegalArgumentException("SectionName could not be empty!");
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) throws IllegalArgumentException {
        if (sectionRepository.findOne(id) == null) {
            throw new IllegalArgumentException("Section with id = " + id + " does not exist in the database");
        }
        if (sectionRepository.findByParentId(id).size() == 0) {
            sectionRepository.delete(id);
        } else {
            logger.info("You can not delete this object. It is associated with other elements.");
        }
    }
}
