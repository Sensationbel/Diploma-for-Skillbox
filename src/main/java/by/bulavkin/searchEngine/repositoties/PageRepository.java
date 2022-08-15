package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.model.PageEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {

    PageEntity findByPath(String path);

    ArrayList<PageEntity> findAllBySiteId(int siteId);
    void deleteAllBySiteId(int siteId);

    Integer countAllBySiteId(int siteId);




//
//    @Query(value = "select * from PageEntity p" +
//            " join IndexEntity i on i.page_id=p.id" +
//            " join LemmaEntity l on l.id=i.lemma_id" +
//            " where l.id = ?1", nativeQuery = true)
//
//    List<PageEntity> findPagesByLemmaId(int lemma_id);
//

}
