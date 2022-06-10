package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.service.IndexServiceImp;
import by.bulavkin.searchEngine.service.LemmaServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Request {

    private final Lemmatizer lm;
    private final Relevance rel;
    private final LemmaServiceImp lsi;
    private final IndexServiceImp isi;

    private final List<String> listLemmas;
    private final List<IndexEntity> indexEntities;
    private final List<LemmaEntity> lemmaEntities;

    public void normaliseRequest(String request) {
        listLemmas.addAll(lm.getListWithNormalFormsFromText(request));
    }

    private List<LemmaEntity> getListWithLemmaEntity() {
        return listLemmas.
                stream().
                map(lsi::findByLemma).
                sorted(Comparator.
                        comparingInt(LemmaEntity::getFrequency)).toList();
    }

    public List<IndexEntity> addIndexEntitiesByLemma() {
        lemmaEntities.addAll(getListWithLemmaEntity());
        Integer flag = lemmaEntities.get(0).getId();

        lemmaEntities.forEach(lemmaEntity -> {
            if (lemmaEntity.getId().equals(flag)) {
                indexEntities.addAll(isi.findByLemmaId(lemmaEntity.getId()));
            } else getIndexEntitiesByNextLemma(lemmaEntity.getId());
        });
        return getSortIndexEntities();
    }

    private List<IndexEntity> getSortIndexEntities() {
        return indexEntities.
                stream().
                sorted(Comparator.
                        comparingInt(IndexEntity::getPageId)).
                toList();
    }

    private void getIndexEntitiesByNextLemma(int lemmaId) {
        List<IndexEntity> indexesList = new ArrayList<>();

        indexEntities.forEach(index -> {
            IndexEntity indexEntity;
            indexEntity = isi.findByLemmaIdAndPageId(lemmaId, index.getPageId());
            if (indexEntity != null) {
                if(!indexesList.contains(indexEntity)) {
                    indexesList.add(indexEntity);
                }
            }
        });
        indexEntities.
                removeIf(entity -> indexesList.
                        stream().
                        noneMatch(index -> index.getPageId().equals(entity.getPageId())));
        indexEntities.addAll(indexesList);
    }

//    public void calculationRelevance(){
//        indexEntities.forEach(index -> {
//            lemmaEntities.forEach(lemma -> {
//                IndexEntity indexEntity = isi.findByLemmaIdAndPageId(lemma.getId(), index.getPageId());
//                PageEntity pageEntity = psi.findById(index.getPageId());
//                rel.addRelevanceList(pageEntity, lemma, indexEntity);
//            });
//        });
//    }

}
