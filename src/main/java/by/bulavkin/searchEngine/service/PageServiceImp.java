package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.repositoties.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PageServiceImp implements PageService{

    private final PageRepository pr;


    @Override
    public PageEntity findByPath(String path) {
        return pr.findByPath(path);
    }

    @Override
    public List<PageEntity> findAll() {
        return null;
    }

    @Override
    public PageEntity findById(Integer pageId) {
        return pr.getById(pageId);
    }

    @Override
    public PageEntity savePageEntity(PageEntity page) {
        return null;
    }

    @Override
    public List<PageEntity> saveALL(List<PageEntity> listPE) {
        return pr.saveAll(listPE);
    }

    @Override
    public PageEntity updatePageEntity(PageEntity page) {
        return null;
    }

    @Override
    public PageEntity deletePageEntity(Integer pageId) {
        return null;
    }
}
