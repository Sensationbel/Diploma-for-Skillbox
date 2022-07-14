package by.bulavkin.searchEngine.dataService.repositoties;

import by.bulavkin.searchEngine.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
}
