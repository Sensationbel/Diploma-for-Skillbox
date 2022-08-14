package by.bulavkin.searchEngine.services.dataService.implementation;

import by.bulavkin.searchEngine.model.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import by.bulavkin.searchEngine.services.dataService.interfeises.LemmaService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LemmaServiceImp implements LemmaService {

    private final LemmaRepository lr;

    @Override
    @Synchronized
    public List<LemmaEntity> saveAll(ArrayList<LemmaEntity> lemmaEntities) {
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
