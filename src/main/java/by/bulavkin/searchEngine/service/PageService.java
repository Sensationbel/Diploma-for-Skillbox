package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.PageEntity;

import java.util.List;

public interface PageService {

    PageEntity findByPath(String path);

    List<PageEntity> findAll();

    PageEntity findById(Integer pageId);

    PageEntity savePageEntity(PageEntity page);

    List<PageEntity> saveALL(List<PageEntity> listPE);

    PageEntity updatePageEntity(PageEntity page);

    PageEntity deletePageEntity(Integer pageId);
}
