package application.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @Column(name = "id")
    @SequenceGenerator(allocationSize = 1, sequenceName = "article_id_seq", name = "article_seq")
    @GeneratedValue(generator = "article_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToMany
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public Article() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "\nArticle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!id.equals(article.id)) return false;
        if (!title.equals(article.title)) return false;
        return name.equals(article.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((title == null) ? 0 : title.hashCode());
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
