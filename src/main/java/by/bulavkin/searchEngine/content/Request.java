package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.service.IndexServiceImp;
import by.bulavkin.searchEngine.service.LemmaServiceImp;
import by.bulavkin.searchEngine.service.PageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Request {

    private final Lemmatizer lm;
    private final LemmaServiceImp lsi;
    private final PageServiceImp psi;
    private final IndexServiceImp isi;

    private List<String> listLemmas;
    private final List<IndexEntity> indexEntities;

    public void normaliseRequest(String request) {
        listLemmas = new ArrayList<>(lm.getListWithNormalFormsFromText(request));
    }

    public List<LemmaEntity> getListWithLemmaEntity() {
        return listLemmas.
                stream().
                map(lsi::findByLemma).
                sorted(Comparator.
                        comparingInt(LemmaEntity::getFrequency)).toList();
    }

    public void addIndexEntities() {
        List<LemmaEntity> lemmaEntities = getListWithLemmaEntity();

        Integer flag = lemmaEntities.get(0).getId();
        lemmaEntities.forEach(lemmaEntity -> {
            if (lemmaEntity.getId().equals(flag)) {
                indexEntities.addAll(isi.findByLemmaId(lemmaEntity.getId()));
            } else getIndexEntityFromIndexEntities(lemmaEntity.getId());
        });
    }

    private void getIndexEntityFromIndexEntities(int lemmaId) {
        List<IndexEntity> indexesList = new ArrayList<>();
        indexEntities.forEach(index -> {
            IndexEntity indexEntity;
            indexEntity = isi.findByLemmaIdAndPageId(lemmaId, index.getPageId());
            if (indexEntity != null) {
                indexesList.add(indexEntity);
            }
        });
        indexEntities.
            removeIf(entity -> indexesList.
                                stream().
                    noneMatch(index -> index.getPageId().equals(entity.getPageId())));
    }

}
