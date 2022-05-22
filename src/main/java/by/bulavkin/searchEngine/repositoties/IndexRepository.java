package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.IndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {

    List<IndexEntity> findByLemmaId(int lemma_id);

    List<IndexEntity> findByPageId(int page_id);

    IndexEntity findByLemmaIdAndPageId(int lemmaId, int pageId);
}
