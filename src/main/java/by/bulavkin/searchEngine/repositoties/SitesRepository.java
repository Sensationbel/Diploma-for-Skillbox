package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.entity.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitesRepository extends JpaRepository<SiteEntity, Integer> {
}
