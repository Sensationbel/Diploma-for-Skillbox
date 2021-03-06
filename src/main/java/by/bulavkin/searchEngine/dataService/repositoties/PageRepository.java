package by.bulavkin.searchEngine.dataService.repositoties;

import by.bulavkin.searchEngine.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {

    PageEntity findByPath(String path);
    ArrayList<PageEntity> findAll();

    @Query("select p from PageEntity p where p.site.id = ?1")
    ArrayList<PageEntity> findAllBySiteId(int siteId);


//
//    @Query(value = "select * from PageEntity p" +
//            " join IndexEntity i on i.page_id=p.id" +
//            " join LemmaEntity l on l.id=i.lemma_id" +
//            " where l.id = ?1", nativeQuery = true)
//
//    List<PageEntity> findPagesByLemmaId(int lemma_id);
//

}
