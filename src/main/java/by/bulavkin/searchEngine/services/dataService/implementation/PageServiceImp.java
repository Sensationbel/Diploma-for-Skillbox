package by.bulavkin.searchEngine.services.dataService.implementation;

import by.bulavkin.searchEngine.config.InstanceForkJoinPool;
import by.bulavkin.searchEngine.model.PageEntity;
import by.bulavkin.searchEngine.repositoties.PageRepository;
import by.bulavkin.searchEngine.services.dataService.interfeises.PageService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PageServiceImp implements PageService {

    private final PageRepository pr;


    @Override
    public PageEntity findByPath(String path) {
        return pr.findByPath(path);
    }

    @Override
    public List<PageEntity> findAll() {
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
    public List<PageEntity> saveAll(Set<PageEntity> listPE) {
        log.info("start saved pagelist, Current Thread: " + Thread.currentThread().getState());
       return pr.saveAll(listPE);
   }

    @Override
    public void deleteAllBySiteId(int siteId) {
        log.info("start deleteAll from pagelist");
        pr.deleteAllBySiteId(siteId);
        log.info("deleteAll from pagelist");
    }

    @Override
    public Integer countAllBySiteId(int siteId) {
        return pr.countAllBySiteId(siteId);
    }


}
