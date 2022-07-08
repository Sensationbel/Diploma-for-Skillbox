package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lemmas")
@Getter
@Setter
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private Integer frequency;

    @ManyToOne
    private SiteEntity site;

    public boolean isEmpty(){
        return lemma == null && frequency == null;
    }

}
