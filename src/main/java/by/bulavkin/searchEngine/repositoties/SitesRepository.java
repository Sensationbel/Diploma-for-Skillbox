package by.bulavkin.searchEngine.repositoties;

import by.bulavkin.searchEngine.model.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SitesRepository extends JpaRepository<SiteEntity, Integer> {

    ArrayList<SiteEntity> findAll();
    void deleteAll();
    SiteEntity findByUrl(String url);
}

