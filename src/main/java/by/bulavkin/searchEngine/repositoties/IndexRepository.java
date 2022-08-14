package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.model.IndexEntity;
import by.bulavkin.searchEngine.model.LemmaEntity;
import by.bulavkin.searchEngine.model.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {

    List<IndexEntity> findByLemmaEntity(LemmaEntity lemmaEntity);

    List<IndexEntity> findAll();

    IndexEntity findByLemmaEntityAndPageEntity(LemmaEntity lemmaEntity, PageEntity pageEntity);
}
