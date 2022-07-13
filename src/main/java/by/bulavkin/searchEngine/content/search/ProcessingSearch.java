package by.bulavkin.searchEngine.content.search;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.service.implementation.IndexServiceImp;
import by.bulavkin.searchEngine.service.implementation.LemmaServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessingSearch {

    private final Lemmatizer lm;
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
                indexEntities.addAll(isi.findByLemmaEntity(lemmaEntity));
            } else getIndexEntitiesByNextLemma(lemmaEntity);
        });
        return getSortIndexEntities();
    }

    private List<IndexEntity> getSortIndexEntities() {
        return indexEntities.
                stream().
                sorted(((o1, o2) -> {
                    if(o1.getPageEntity().getId()
                            .equals(o2.getPageEntity().getId())){
                        return o1.getId().compareTo(o2.getId());
                    }
                    else if(o1.getPageEntity().getId() > o2.getPageEntity().getId()){
                        return 1;
                    }  else return -1;
                })).
                toList();
    }

    private void getIndexEntitiesByNextLemma(LemmaEntity lemma) {
        List<IndexEntity> indexesList = new ArrayList<>();

        indexEntities.forEach(index -> {
            IndexEntity indexEntity;
            indexEntity = isi.findByLemmaEntityAndPageEntity(lemma, index.getPageEntity());
            if (indexEntity != null) {
                if(!indexesList.contains(indexEntity)) {
                    indexesList.add(indexEntity);
                }
            }
        });
        indexEntities.
                removeIf(entity -> indexesList.
                        stream().
                        noneMatch(index -> index.
                                getPageEntity().
                                getId().
                                equals(entity.
                                        getPageEntity().
                                        getId())));
        indexEntities.addAll(indexesList);
    }
}
