package by.bulavkin.searchEngine.content.statistics;

import by.bulavkin.searchEngine.content.statistics.detailed.Detailed;
import by.bulavkin.searchEngine.content.statistics.detailed.Total;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.service.SitesServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class Statistics {

    private final SitesServiceImpl ssi;
    private ArrayList<SiteEntity> siteList;

    public StringBuilder getStatistics() {
        StringBuilder builder = new StringBuilder();
        siteList = new ArrayList<>(ssi.findAll());

        if (siteList.isEmpty()) {
            ResultIndexing result = new ResultIndexing("Индексация не запущена");
            builder.append(result.getResult());
            return builder;
        } else return addDetailed(builder);
    }

    private Total addTotal() {
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

    private StringBuilder addDetailed(StringBuilder builder) {
        ArrayList<Detailed> detailed = addDetailedList();
        ResultIndexing result = new ResultIndexing();
        builder.append(result.getResult()).
                append("\"statistics\": ").
                append(addTotal().toString()).
                append("\"detailed\": [");

        for (int i = 0; i < detailed.size(); i++) {
            if (i != (detailed.size() - 1)) {
                builder.append(detailed.get(i).toString()).append(", ");
            } else builder.append(detailed.get(i).toString()).append("]}}");
        }
        return builder;
    }


}
