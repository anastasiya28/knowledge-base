package application.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "section")
public class Section {
    @Id
    @Column(name = "id")
    @SequenceGenerator(allocationSize = 1, sequenceName = "section_id_seq", name = "section_seq")
    @GeneratedValue(generator = "section_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "section")
    private Set<Article> articles;

    public Section() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "\nSection{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (!id.equals(section.id)) return false;
        if (!parentId.equals(section.parentId)) return false;
        return name.equals(section.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
