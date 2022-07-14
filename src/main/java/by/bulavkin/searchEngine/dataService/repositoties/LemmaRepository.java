package by.bulavkin.searchEngine.dataService.repositoties;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {

    LemmaEntity findByLemma(String lemma);
}
