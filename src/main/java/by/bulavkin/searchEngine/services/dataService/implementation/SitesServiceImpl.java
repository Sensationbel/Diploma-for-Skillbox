package by.bulavkin.searchEngine.services.dataService.implementation;

import by.bulavkin.searchEngine.model.SiteEntity;
import by.bulavkin.searchEngine.repositoties.SitesRepository;
import by.bulavkin.searchEngine.services.dataService.interfeises.SitesService;
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
    public SiteEntity findByUrl(String url) {
        return sr.findByUrl(url);
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
