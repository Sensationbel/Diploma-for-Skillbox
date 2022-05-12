package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.repositoties.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexServiceImp implements IndexService{

    private final IndexRepository ir;

    public void saveAll(List<IndexEntity> indexes){
        ir.saveAll(indexes);
    }
}
