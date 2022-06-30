package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "sites")
public class SiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "status_time", nullable = false)
    private long statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @OneToMany
    @JoinColumn(name = "site_id")
    private List<PageEntity> pageEntities;

    @OneToMany
    @JoinColumn(name = "site_Id")
    private List<LemmaEntity> lemmaEntities;


    public boolean isEmpty(){
        return url == null && name == null;
    }
}
