package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.FieldEntity;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceIml implements FieldService{

    private final FieldRepository fr;

    @Override
    public List<FieldEntity> findAll() {
        return fr.findAll();
    }
}
