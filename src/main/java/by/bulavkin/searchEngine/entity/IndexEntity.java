package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "indexes")
@Getter
@Setter
@RequiredArgsConstructor
public class IndexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private PageEntity pageEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id")
    private LemmaEntity lemmaEntity;

    @Column(name = "ranks", nullable = false)
    private Float rank;

    public boolean isEmpty() {
        return pageEntity == null &&
                lemmaEntity == null &&
                rank == null;
    }
}
