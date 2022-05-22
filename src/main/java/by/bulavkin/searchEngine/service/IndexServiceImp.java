package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.repositoties.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexServiceImp implements IndexService{

    private final IndexRepository ir;

    @Override
    public void saveAll(List<IndexEntity> indexes) {
        ir.saveAll(indexes);
    }

    @Override
    public List<IndexEntity> findByLemmaId(int lemma_id) {
        return ir.findByLemmaId(lemma_id);
    }

    @Override
    public List<IndexEntity> findByPageId(int page_id) {
        return ir.findByPageId(page_id);
    }

    @Override
    public IndexEntity findByLemmaIdAndPageId(int lemmaId, int pageId) {
        return ir.findByLemmaIdAndPageId(lemmaId, pageId);
    }
}
