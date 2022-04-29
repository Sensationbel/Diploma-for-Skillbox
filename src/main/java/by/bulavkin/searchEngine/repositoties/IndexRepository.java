package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.IndexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends JpaRepository<IndexEntity, Integer> {
}
