package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.Sites;
import by.bulavkin.searchEngine.repositoties.SitesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SitesServiceImpl implements SitesService{

    private final SitesRepository sr;
    @Override
    public List<Sites> saveALL(List<Sites> sitesList) {
        return sr.saveAll(sitesList);
    }
}