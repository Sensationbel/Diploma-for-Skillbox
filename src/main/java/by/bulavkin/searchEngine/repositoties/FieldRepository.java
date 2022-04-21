package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
//@Transactional
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {

//    @Modifying
//    @Query(value =
//            "insert into field_entity (id, name, selector, weight) values (:id, :name, :selector, :weight)",
//            nativeQuery = true)
    void insertFieldEntity(Integer id, String name, String selector, Float weight);
}
