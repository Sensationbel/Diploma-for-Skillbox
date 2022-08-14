package by.bulavkin.searchEngine.services.dataService.interfeises;

import by.bulavkin.searchEngine.model.IndexEntity;
import by.bulavkin.searchEngine.model.LemmaEntity;
import by.bulavkin.searchEngine.model.PageEntity;

import java.util.List;

public interface IndexService {

    void saveAll(List<IndexEntity> indexes);

    List<IndexEntity> findByLemmaEntity(LemmaEntity lemmaEntity);

    List<IndexEntity> findAll();

    IndexEntity findByLemmaEntityAndPageEntity(LemmaEntity lemmaEntity, PageEntity pageEntity);

}
