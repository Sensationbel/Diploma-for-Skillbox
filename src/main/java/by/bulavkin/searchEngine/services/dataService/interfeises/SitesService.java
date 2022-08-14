package by.bulavkin.searchEngine.services.dataService.interfeises;

import by.bulavkin.searchEngine.model.SiteEntity;

import java.util.ArrayList;
import java.util.List;

public interface SitesService {

    List<SiteEntity> saveALL(List<SiteEntity> siteEntityList);
    SiteEntity save(SiteEntity site);
    SiteEntity findByUrl(String url);
    ArrayList<SiteEntity> findAll();
    void deleteAll();
}
