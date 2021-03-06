package by.bulavkin.searchEngine.dataService.implementation;

import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.dataService.repositoties.SitesRepository;
import by.bulavkin.searchEngine.dataService.interfeises.SitesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SitesServiceImpl implements SitesService {

    private final SitesRepository sr;

    @Override
    public List<SiteEntity> saveALL(List<SiteEntity> siteEntityList) {
        return sr.saveAll(siteEntityList);
    }

    @Override
    public SiteEntity save(SiteEntity site) {
        return sr.save(site);
    }

    @Override
    public ArrayList<SiteEntity> findAll() {
        return sr.findAll();
    }

    @Override
    public void deleteAll() {
        sr.deleteAll();
    }
}
