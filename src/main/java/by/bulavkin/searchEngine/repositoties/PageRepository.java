package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {

    PageEntity findByPath(String path);
//
//    @Query(value = "select * from PageEntity p" +
//            " join IndexEntity i on i.page_id=p.id" +
//            " join LemmaEntity l on l.id=i.lemma_id" +
//            " where l.id = ?1", nativeQuery = true)
//
//    List<PageEntity> findPagesByLemmaId(int lemma_id);
//

}
