package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService{

    private final LemmaRepository lr;
    private Set<LemmaEntity> lemmas = new HashSet<>();

    @Override
    public void saveAll(Set<LemmaEntity> lemmaEntities) {
        lr.saveAll(lemmaEntities);
    }

    @Override
    public LemmaEntity findByLemma(String lemma) {
        return lr.findByLemma(lemma);
    }

    @Override
    public LemmaEntity findById(Integer id) {
        return lr.getById(id);
    }
}
