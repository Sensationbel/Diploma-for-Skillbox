package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService{

    private final LemmaRepository lr;

    @Override
    public void saveLemmaEntity(Map<String, Integer> mapLemas) {
        final boolean[] isVisited = {true};
        mapLemas.forEach((k, v) -> {
            LemmaEntity lemmaEntities = findByLemma(k);
            if(lemmaEntities == null){
                isVisited[0] = false;
                LemmaEntity lemmaEntity = new LemmaEntity();
                lemmaEntity.setLemma(k);
                lemmaEntity.setFrequency(1);
                lr.save(lemmaEntity);
            } else if(isVisited[0]){
                int id = lemmaEntities.getId();
                LemmaEntity lemmaEntity = findById(id);
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
                lr.save(lemmaEntity);
            }
        });
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
