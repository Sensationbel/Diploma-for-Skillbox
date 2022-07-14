package by.bulavkin.searchEngine.dataService.interfeises;

import by.bulavkin.searchEngine.entity.PageEntity;

import java.util.ArrayList;
import java.util.List;

public interface PageService {

    PageEntity findByPath(String path);

    Iterable<PageEntity> findAll();

    PageEntity findById(Integer pageId);

    ArrayList<PageEntity> findAllBySiteId(int siteId);

    void saveALL(List<PageEntity> listPE);
}
