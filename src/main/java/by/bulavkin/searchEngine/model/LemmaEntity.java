package by.bulavkin.searchEngine.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Table(name = "lemma")
@SQLInsert(sql = "INSERT INTO lemma (frequency, lemma, site_id) VALUES (?,?,?) " +
        "ON DUPLICATE KEY UPDATE frequency = frequency + 1")
@Getter
@Setter
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private Integer frequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private SiteEntity site;

    public boolean isEmpty(){
        return lemma == null && frequency == null;
    }

}
