package by.bulavkin.searchEngine.services.dataService.interfeises;

import by.bulavkin.searchEngine.model.LemmaEntity;

import java.util.ArrayList;
import java.util.List;

public interface LemmaService {

    List<LemmaEntity> saveAll(ArrayList<LemmaEntity> lemmaEntities);
    List<LemmaEntity> findAll();
    LemmaEntity findByLemma(String lemma);
    void deleteAllBySiteId(int siteId);
    LemmaEntity findById(int id);
    Integer countAllBySiteId(int siteId);


}
