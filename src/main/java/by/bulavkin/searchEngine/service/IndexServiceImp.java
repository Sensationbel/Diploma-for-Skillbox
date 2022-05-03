package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.repositoties.LemmaRepository;
import by.bulavkin.searchEngine.repositoties.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexServiceImp implements IndexService{

    private final PageRepository rp;
    private final LemmaRepository lr;
    private List<IndexEntity> indexes = new ArrayList<>();

    public void addIndexes(){

    }

    private Integer getIdPages(String path){
        String clearPath = deliteRootDirectoty(path);
        PageEntity pageEntity = rp.findByPath(path);
        return pageEntity.getId();
    }

    private String deliteRootDirectoty(String path) {
        String regex = "(https?|HTTPS?)://.+?/";
        return path.replaceAll(regex, "/");
    }
}
