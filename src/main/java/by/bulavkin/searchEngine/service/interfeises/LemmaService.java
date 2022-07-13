package by.bulavkin.searchEngine.service.interfeises;

import by.bulavkin.searchEngine.entity.LemmaEntity;

import java.util.List;

public interface LemmaService {

    List<LemmaEntity> saveAll(List<LemmaEntity> lemmaEntities);

    List<LemmaEntity> findAll();

    LemmaEntity findByLemma(String lemma);

    LemmaEntity findById(Integer id);
}
