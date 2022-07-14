package by.bulavkin.searchEngine.dataService.implementation;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.dataService.repositoties.IndexRepository;
import by.bulavkin.searchEngine.dataService.interfeises.IndexService;
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
    public List<IndexEntity> findByPageEntity(PageEntity page) {
        return ir.findByPageEntity(page);
    }

    @Override
    public IndexEntity findByLemmaEntityAndPageEntity(LemmaEntity lemma, PageEntity page) {
        return ir.findByLemmaEntityAndPageEntity(lemma, page);
    }
}
