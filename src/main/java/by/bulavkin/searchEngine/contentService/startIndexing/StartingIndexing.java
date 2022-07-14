package by.bulavkin.searchEngine.contentService.startIndexing;

import by.bulavkin.searchEngine.contentService.statistics.ResultIndexing;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.entity.Status;
import by.bulavkin.searchEngine.parsing.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.dataService.implementation.PageServiceImp;
import by.bulavkin.searchEngine.dataService.implementation.SitesServiceImpl;
import by.bulavkin.searchEngine.dataService.mySingletone.MyForkJoinPool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class StartingIndexing {

    private final WebLinkParser webLinkParser;
    private final GettingLemmas gettingLemmas;
    private final SitesServiceImpl ssi;
    private final DataToParse dataToParse;


    private List<CompletableFuture<Void>> futureList;
    private volatile boolean isLock = true;

    public String startIndexing() {

        if (isLock) {
            log.info("Running parsing");
            List<SiteEntity> sites = addDataToSitesEntity();
            futureList = new ArrayList<>();
            isLock = false;
            try {
                futureList = sites.
                        stream().map(site -> createCompletableFuture(site)).toList();
                return new ResultIndexing().getResults();
            } catch (Exception ex) {
                return new ResultIndexing("Ошибка индексации").getResults();
            }
        } else
            return new ResultIndexing("Идет индексирование").getResults();
    }

    public void getResultFromFuture() {
        futureList.forEach(f -> {
            f.join();
        });
        isLock = true;

    }

    private CompletableFuture<Void> createCompletableFuture(SiteEntity site) {
        return CompletableFuture.runAsync(() -> {
            webLinkParser.start(site);
        }, MyForkJoinPool.getMyForkJoinPool()).
                thenRunAsync(() -> gettingLemmas.
                        startAddContentToDatabase(site),
                        MyForkJoinPool.getMyForkJoinPool());
    }

    private List<SiteEntity> addDataToSitesEntity() {
        List<SiteEntity> list = new ArrayList<>();

        dataToParse.getSites().forEach(s -> {
            SiteEntity site = new SiteEntity();
            site.setName(s.getName());
            site.setUrl(s.getUrl());
            site.setStatus(Status.INDEXING);
            site.setStatusTime(System.currentTimeMillis());
            list.add(site);
        });
        return ssi.saveALL(list);
    }
}
