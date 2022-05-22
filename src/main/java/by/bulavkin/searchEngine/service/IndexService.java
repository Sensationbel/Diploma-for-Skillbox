package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;

import java.util.List;

public interface IndexService {

    void saveAll(List<IndexEntity> indexes);

    List<IndexEntity> findByLemmaId(int lemma_id);

    List<IndexEntity> findByPageId(int page_id);

    IndexEntity findByLemmaIdAndPageId(int lemmaId, int pageId);

}
