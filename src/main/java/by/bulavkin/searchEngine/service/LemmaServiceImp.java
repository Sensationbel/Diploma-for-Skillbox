package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService{

    private final LemmaRepository lp;

    @Override
    public void saveLemmaEntity(Map<String, Integer> mapLemas) {
        final boolean[] isVisited = {true};
        mapLemas.forEach((k, v) -> {
            List<LemmaEntity> lemmaEntities = findByLemma(k);
            if(lemmaEntities == null || lemmaEntities.isEmpty()){
                isVisited[0] = false;
                LemmaEntity lemmaEntity = new LemmaEntity();
                lemmaEntity.setLemma(k);
                lemmaEntity.setFrequency(1);
                lp.save(lemmaEntity);
            } else if(isVisited[0]){
                int id = lemmaEntities.stream().map(l -> l.getId()).findFirst().get();
                LemmaEntity lemmaEntity = findById(id);
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
                lp.save(lemmaEntity);
            }
        });
    }

    @Override
    public List<LemmaEntity> findByLemma(String lemma) {
        var lemmas = lp.findByLemma(lemma);
        return lemmas;
    }

    @Override
    public LemmaEntity findById(Integer id) {
        return lp.getById(id);
    }
}
