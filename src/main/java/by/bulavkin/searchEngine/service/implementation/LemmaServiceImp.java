package by.bulavkin.searchEngine.service.implementation;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import by.bulavkin.searchEngine.service.interfeises.LemmaService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService {

    private final LemmaRepository lr;

    @Override
    @Synchronized
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
