package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;

import java.util.List;
import java.util.Map;

public interface LemmaService {

    void saveLemmaEntity(Map<String, Integer> mapLemmas);

    List findByLemma(String lemma);

    LemmaEntity findById(Integer id);
}
