package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.LemmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
}
