package by.bulavkin.searchEngine.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Sites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "status_time", nullable = false)
    private long statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT", nullable = false)
    private String lastError;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;
}
