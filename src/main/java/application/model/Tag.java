package application.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name = "id")
    @SequenceGenerator(allocationSize = 1, sequenceName = "tag_id_seq", name = "tag_seq")
    @GeneratedValue(generator = "tag_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = @JoinColumn(name = "article_id"))
    private Set<Article> articles;

    public Tag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "\nTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!id.equals(tag.id)) return false;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
