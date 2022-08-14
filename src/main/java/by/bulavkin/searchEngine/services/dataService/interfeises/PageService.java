package by.bulavkin.searchEngine.services.dataService.interfeises;

import by.bulavkin.searchEngine.model.PageEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PageService {

    PageEntity findByPath(String path);

    List<PageEntity> findAll();

    PageEntity findById(Integer pageId);

    ArrayList<PageEntity> findAllBySiteId(int siteId);

    List<PageEntity> saveAll(Set<PageEntity> listPE);

    void deleteAllBySiteId(int siteId);
}
