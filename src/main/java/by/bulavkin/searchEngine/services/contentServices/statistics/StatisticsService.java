package by.bulavkin.searchEngine.services.contentServices.statistics;

import by.bulavkin.searchEngine.dto.statistics.*;
import by.bulavkin.searchEngine.dto.errors.StatisticErrors;
import by.bulavkin.searchEngine.services.contentServices.startIndexing.StartingIndexingService;
import by.bulavkin.searchEngine.model.SiteEntity;
import by.bulavkin.searchEngine.services.dataService.implementation.SitesServiceImpl;
import by.bulavkin.searchEngine.services.dataService.interfeises.IndexService;
import by.bulavkin.searchEngine.services.dataService.interfeises.SitesService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

    private final StartingIndexingService startingIndexing;
    private final SitesService ssi;
    private final IndexService is;
    private List<SiteEntity> siteList;


    public StatisticsInterface getALLStatistics() {
        siteList = new ArrayList<>(ssi.findAll());
        if (is.findAll().isEmpty()) {
            log.info("DB is empty");
            return new StatisticErrors("База данных не заполнена");
        } else {
            startingIndexing.getResultFromFuture();
            return getStatisticsDto();
        }
    }

    private StatisticsDTO getStatisticsDto() {
        return new StatisticsDTO(true, addDetailed());
    }

    private Statistics addDetailed() {
        ArrayList<Detailed> detailed = addDetailedList();
        Total total = addTotal();
        return new Statistics(total, detailed);
    }

    private Total addTotal() {
        Total total = new Total();
        total.setSites(siteList.size());

        int countPages = (int) siteList.
                stream().
                flatMap(s -> s.getPageEntities().
                        stream()).
                count();
        total.setPages(countPages);
        int countLemmas = (int) siteList.
                stream().
                flatMap(s -> s.getLemmaEntities().
                        stream()).
                count();
        total.setLemmas(countLemmas);
        total.setIndexing(true);
        return total;
    }

    private ArrayList<Detailed> addDetailedList() {
        ArrayList<Detailed> detailed = new ArrayList<>();

        siteList.forEach(s -> {
            Detailed d = new Detailed();
            d.setUrl(s.getUrl());
            d.setName(s.getName());
            d.setStatus(s.getStatus());
            d.setError(s.getLastError());
            d.setStatusTime(s.getStatusTime());
            d.setPages(s.getPageEntities().size());
            d.setLemmas(s.getLemmaEntities().size());
            detailed.add(d);
        });
        return detailed;
    }

}
