package by.bulavkin.searchEngine.dataService.repositoties;

import by.bulavkin.searchEngine.entity.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SitesRepository extends JpaRepository<SiteEntity, Integer> {

    ArrayList<SiteEntity> findAll();
    void deleteAll();
}
