package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.IndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {
}
