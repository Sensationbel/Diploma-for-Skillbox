package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Integer> {

    PageEntity findByPath(String path);
}
