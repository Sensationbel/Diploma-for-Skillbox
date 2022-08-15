package by.bulavkin.searchEngine.services.contentServices.statistics;

import by.bulavkin.searchEngine.dto.statistics.*;
import by.bulavkin.searchEngine.dto.errors.StatisticErrors;
import by.bulavkin.searchEngine.services.contentServices.startIndexing.StartingIndexingService;
import by.bulavkin.searchEngine.model.SiteEntity;
import by.bulavkin.searchEngine.services.dataService.interfeises.IndexService;
import by.bulavkin.searchEngine.services.dataService.interfeises.LemmaService;
import by.bulavkin.searchEngine.services.dataService.interfeises.PageService;
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
    private final SitesService si;
    private final LemmaService ls;
    private final PageService ps;
    private final IndexService is;
    private List<SiteEntity> siteList;


    public StatisticsInterface getALLStatistics() {
        siteList = new ArrayList<>(si.findAll());
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

        int countPages = siteList.stream().mapToInt(siteEntity -> ps.countAllBySiteId(siteEntity.getId())).sum();
        total.setPages(countPages);
        int countLemmas = siteList.stream().mapToInt(siteEntity -> ls.countAllBySiteId(siteEntity.getId())).sum();
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
            d.setPages(ps.countAllBySiteId(s.getId()));
            d.setLemmas(ls.countAllBySiteId(s.getId()));
            detailed.add(d);
        });
        return detailed;
    }

}
