package by.bulavkin.searchEngine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "field")
@Getter
@Setter
public class FieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
    private String selector;

    @Column(nullable = false)
    private Float weight;
}
