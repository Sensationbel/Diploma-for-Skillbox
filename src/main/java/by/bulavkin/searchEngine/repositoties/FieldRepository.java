package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.model.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Integer> {
}
