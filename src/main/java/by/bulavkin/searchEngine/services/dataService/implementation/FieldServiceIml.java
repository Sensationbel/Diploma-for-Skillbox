package by.bulavkin.searchEngine.services.dataService.implementation;

import by.bulavkin.searchEngine.model.FieldEntity;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import by.bulavkin.searchEngine.services.dataService.interfeises.FieldService;
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
