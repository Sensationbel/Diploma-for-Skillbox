package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.repositoties.SitesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SitesServiceImpl implements SitesService{

    private final SitesRepository sr;
    @Override
    public List<SiteEntity> saveALL(List<SiteEntity> siteEntityList) {
        return sr.saveAll(siteEntityList);
    }

    @Override
    public SiteEntity save(SiteEntity site) {
        return sr.save(site);
    }
}
