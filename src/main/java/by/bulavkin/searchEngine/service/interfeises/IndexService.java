package by.bulavkin.searchEngine.service.interfeises;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.entity.PageEntity;

import java.util.List;

public interface IndexService {

    void saveAll(List<IndexEntity> indexes);

    List<IndexEntity> findByLemmaEntity(LemmaEntity lemmaEntity);

    List<IndexEntity> findByPageEntity(PageEntity pageEntity);

    IndexEntity findByLemmaEntityAndPageEntity(LemmaEntity lemmaEntity, PageEntity pageEntity);

}
