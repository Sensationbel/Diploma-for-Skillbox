package by.bulavkin.searchEngine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLInsert;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Table(name = "page")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLInsert(sql = "INSERT INTO `page`(`code`, content, `path`, site_id)" +
        " VALUES(?, ?, ?, ?)" +
        " ON DUPLICATE KEY UPDATE content=content")
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    //    @Column(columnDefinition = "TEXT", nullable = false)
    @EqualsAndHashCode.Include
    private String path;

    //    @Column(nullable = false)
    private Integer code;

    //    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "site_id")
    private SiteEntity site;

}
