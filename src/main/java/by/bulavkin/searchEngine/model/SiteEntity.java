package by.bulavkin.searchEngine.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "site")
@SQLInsert(sql = "INSERT INTO site ( last_error, `name`,`status`, status_time, url)" +
        " VALUES (?,?,?,?,?)" +
        " ON DUPLICATE KEY UPDATE `status`=`status`")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM", nullable = false)
    private Status status;

    @Column(name = "status_time", nullable = false)
    private Timestamp statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "site")
    private List<PageEntity> pageEntities;

    @OneToMany(mappedBy="site")
    private List<LemmaEntity> lemmaEntities;

    public boolean isEmpty(){
        return url == null && name == null;
    }

    public enum Status {
        INDEXING,
        INDEXED,
        FILED
    }
}

