package by.bulavkin.searchEngine.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFromUrlRepository extends CrudRepository<DataFromUrl, Integer> {
}
