package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.repositoties.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class IndexServiceImp implements IndexService{

    private final IndexRepository ir;

    public void saveIndexesList(Set<IndexEntity> indexes){
        ir.saveAll(indexes);
    }
}
