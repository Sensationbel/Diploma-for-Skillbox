package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LemmaService {

    void saveLemmaEntity(Map<String, Integer> mapLemmas);

    LemmaEntity findByLemma(String lemma);

    LemmaEntity findById(Integer id);
}
