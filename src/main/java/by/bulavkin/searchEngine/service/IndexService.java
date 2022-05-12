package by.bulavkin.searchEngine.service;

import by.bulavkin.searchEngine.entity.IndexEntity;

import java.util.List;

public interface IndexService {
    void saveAll(List<IndexEntity> indexes);

}
