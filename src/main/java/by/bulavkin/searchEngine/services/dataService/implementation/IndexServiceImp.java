package by.bulavkin.searchEngine.services.dataService.implementation;

import by.bulavkin.searchEngine.model.IndexEntity;
import by.bulavkin.searchEngine.model.LemmaEntity;
import by.bulavkin.searchEngine.model.PageEntity;
import by.bulavkin.searchEngine.repositoties.IndexRepository;
import by.bulavkin.searchEngine.services.dataService.interfeises.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexServiceImp implements IndexService {

    private final IndexRepository ir;

    @Override
    public void saveAll(List<IndexEntity> indexes) {
        ir.saveAll(indexes);
    }

    @Override
    public List<IndexEntity> findByLemmaEntity(LemmaEntity lemma) {
        return ir.findByLemmaEntity(lemma);
    }

    @Override
    public List<IndexEntity> findAll() {
        return ir.findAll();
    }

    @Override
    public IndexEntity findByLemmaEntityAndPageEntity(LemmaEntity lemma, PageEntity page) {
        return ir.findByLemmaEntityAndPageEntity(lemma, page);
    }
}
