package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.PageEntity;

import java.util.List;

public interface PageService {

    List<PageEntity> findAll();

    PageEntity findById(Integer pageId);

    PageEntity saveTodo(PageEntity page);

    PageEntity updateTodo(PageEntity page);

    PageEntity deleteTodo(Integer pageId);
}
