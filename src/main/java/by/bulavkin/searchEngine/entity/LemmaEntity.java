package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lemmas", indexes = @Index(name = "id" ,columnList = "id"))
@Getter
@Setter
@RequiredArgsConstructor
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private Integer frequency;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;

    @Transient
    private Integer pageId;

    public boolean isEmpty(){
        return lemma == null && frequency == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "lemma = " + lemma + ", " +
                "frequency = " + frequency +
                "site_id" + site.getId() +")";
    }
}
