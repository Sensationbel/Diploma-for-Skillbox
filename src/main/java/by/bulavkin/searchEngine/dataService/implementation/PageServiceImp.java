package by.bulavkin.searchEngine.dataService.implementation;

import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.dataService.repositoties.PageRepository;
import by.bulavkin.searchEngine.dataService.interfeises.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageServiceImp implements PageService {

    private final PageRepository pr;


    @Override
    public PageEntity findByPath(String path) {
        return pr.findByPath(path);
    }

    @Override
    public ArrayList<PageEntity> findAll() {
        return pr.findAll();
    }

    @Override
    public PageEntity findById(Integer pageId) {
        return pr.getById(pageId);
    }

    @Override
    public ArrayList<PageEntity> findAllBySiteId(int siteId) {
        return pr.findAllBySiteId(siteId);
    }

    @Override
    public void saveALL(List<PageEntity> listPE) {
       pr.saveAll(listPE);
   }


}
