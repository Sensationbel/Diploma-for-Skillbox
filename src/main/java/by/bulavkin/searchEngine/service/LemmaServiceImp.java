package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService{

    private final LemmaRepository lr;

    @Override
    public List<LemmaEntity> saveAll(List<LemmaEntity> lemmaEntities) {
        return lr.saveAll(lemmaEntities);
    }

    @Override
    public List<LemmaEntity> findAll() {
        return lr.findAll();
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
