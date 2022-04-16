package by.bulavkin.searchEngine.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "page_id", nullable = false)
    private Integer pageId;

    @Column(name = "lemma_id", nullable = false)
    private Integer lemmaId;

    @Column(nullable = false)
    private Float rank;

}
