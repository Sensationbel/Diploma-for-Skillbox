package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.PageEntity;

import java.util.List;

public interface PageService {

    PageEntity findByPath(String path);

    List<PageEntity> findAll();

    PageEntity findById(Integer pageId);

    void saveALL(List<PageEntity> listPE);
}
