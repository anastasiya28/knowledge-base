package application.service.interfaces;

import application.model.Section;

import java.util.List;

public interface SectionService {
    List<Section> findAll();

    Section findById(Integer id);

    List<Section> findByParentId(Integer id);

    public Section add(Section section, Integer psrentId);

    Section update(Section section);

    void delete(Integer id);
}
