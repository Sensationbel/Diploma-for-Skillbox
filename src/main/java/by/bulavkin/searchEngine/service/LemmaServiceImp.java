package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LemmaServiceImp implements LemmaService{

    private final LemmaRepository lp;
    private final EntityManagerFactory emf;

    @Override
    public void saveLemmaEntity(Map<String, Integer> mapLemas) {
        mapLemas.forEach((k, v) -> {
            List<LemmaEntity> lemmaEntities = findByLemma(k);
            if(lemmaEntities == null || lemmaEntities.isEmpty()){
                LemmaEntity lemmaEntity = new LemmaEntity();
                lemmaEntity.setLemma(k);
                lemmaEntity.setFrequency(1);
                lp.save(lemmaEntity);
            } else {
                int id = lemmaEntities.stream().map(l -> l.getId()).findFirst().get();
                LemmaEntity lemmaEntity = findById(id);
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
                lp.save(lemmaEntity);
            }
        });
    }

    @Override
    public List<LemmaEntity> findByLemma(String lemma) {
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createQuery("Select l from LemmaEntity l where l.lemma=:lemma");
        query.setParameter("lemma", lemma);
        return query.getResultList();
    }

    @Override
    public LemmaEntity findById(Integer id) {
        return lp.getById(id);
    }
}
