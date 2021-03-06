package by.bulavkin.searchEngine.dataService.implementation;

import by.bulavkin.searchEngine.entity.FieldEntity;
import by.bulavkin.searchEngine.dataService.repositoties.FieldRepository;
import by.bulavkin.searchEngine.dataService.interfeises.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceIml implements FieldService {

    private final FieldRepository fr;

    @Override
    public List<FieldEntity> findAll() {
        return fr.findAll();
    }
}
