package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.PageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFromUrlRepository extends CrudRepository<PageEntity, Integer> {
}
