package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LemmaService {

    List<LemmaEntity> saveAll(List<LemmaEntity> lemmaEntities);

    List<LemmaEntity> findAll();

    LemmaEntity findByLemma(String lemma);

    LemmaEntity findById(Integer id);
}
