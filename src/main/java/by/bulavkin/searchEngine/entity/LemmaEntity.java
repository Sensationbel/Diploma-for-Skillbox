package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
@SQLInsert(sql = "INSERT INTO Lemma (id, lemms, frequency, site_id) VALUES (?,?,?) AS new(a, b, c, d)" +
        "ON DUPLICATE KEY UPDATE frequency=frequency+new.c")
@Getter
@Setter
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
