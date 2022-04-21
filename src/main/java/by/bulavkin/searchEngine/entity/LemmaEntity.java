package by.bulavkin.searchEngine.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lemmas")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private Integer frequency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LemmaEntity lemma = (LemmaEntity) o;
        return id != null && Objects.equals(id, lemma.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
