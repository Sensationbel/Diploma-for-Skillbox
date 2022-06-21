package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.Sites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitesRepository extends JpaRepository<Sites, Integer> {
}
