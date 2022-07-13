package by.bulavkin.searchEngine.service.interfeises;

import by.bulavkin.searchEngine.entity.SiteEntity;

import java.util.ArrayList;
import java.util.List;

public interface SitesService {

    List<SiteEntity> saveALL(List<SiteEntity> siteEntityList);
    SiteEntity save(SiteEntity site);
    ArrayList<SiteEntity> findAll();
    void deleteAll();
}
