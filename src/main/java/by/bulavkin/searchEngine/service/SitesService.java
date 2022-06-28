package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.SiteEntity;

import java.util.List;

public interface SitesService {

    List<SiteEntity> saveALL(List<SiteEntity> siteEntityList);
    SiteEntity save(SiteEntity site);
}
