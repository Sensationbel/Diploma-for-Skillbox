package by.bulavkin.searchEngine.contentService.statistics;

import by.bulavkin.searchEngine.contentService.startIndexing.StartingIndexing;
import by.bulavkin.searchEngine.contentService.statistics.detailed.Detailed;
import by.bulavkin.searchEngine.contentService.statistics.detailed.Total;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.dataService.implementation.SitesServiceImpl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class Statistics {

    @Autowired
    private StartingIndexing startingIndexing;
    @Autowired
    private Total total;
    @Autowired
    private SitesServiceImpl ssi;
    private List<Detailed> detailed;

    public Statistics(Total total, List<Detailed> detailed) {
        this.total = total;
        this.detailed = detailed;
    }

    public StringBuilder getALLStatistics() {
        ResultIndexing resultIndexing;
        StringBuilder statistic = new StringBuilder();
        List<SiteEntity> siteList = new ArrayList<>(ssi.findAll());

        if (siteList.isEmpty()) {
            resultIndexing = new ResultIndexing("База данных не заполнена");
            return statistic.append(resultIndexing.getResultForStatistics() );

        } else {
            startingIndexing.getResultFromFuture();
            resultIndexing = new ResultIndexing();
            return statistic.
                    append(resultIndexing.getResultForStatistics()).
                    append(addDetailed(siteList));
        }
    }

    private Total addTotal(List<SiteEntity> siteList) {
        Total total = new Total();
        total.setSites(siteList.size());

        int countP = (int) siteList.
                stream().
                flatMap(s -> s.getPageEntities().
                        stream()).
                count();
        total.setPages(countP);
        int countL = (int) siteList.
                stream().
                flatMap(s -> s.getLemmaEntities().
                        stream()).
                count();
        total.setLemmas(countL);
        total.setIndexing(true);
        return total;
    }

    private ArrayList<Detailed> addDetailedList(List<SiteEntity> siteList) {
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

    private Statistics addDetailed(List<SiteEntity> siteList) {
        ArrayList<Detailed> detailed = addDetailedList(siteList);
        Total total = addTotal(siteList);
        return new Statistics(total, detailed);
    }

    @Override
    public String toString() {
        return "\"statistics\": {" +
                "\"total\": " + total +
                "\"detailed\": " + detailed +
                "}}";
    }
}
