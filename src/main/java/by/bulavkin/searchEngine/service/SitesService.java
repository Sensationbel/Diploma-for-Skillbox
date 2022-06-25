package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.Sites;

import java.util.List;

public interface SitesService {

    List<Sites> saveALL(List<Sites> sitesList);
    Sites save(Sites site);
}
