package by.bulavkin.searchEngine.content.start;

import by.bulavkin.searchEngine.content.statistics.ResultIndexing;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.entity.Status;
import by.bulavkin.searchEngine.parsing.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.implementation.PageServiceImp;
import by.bulavkin.searchEngine.service.implementation.SitesServiceImpl;
import by.bulavkin.searchEngine.service.mySingletone.MyReentrantLock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class StartingIndexing {

    private final PageServiceImp psi;
    private final GettingLemmas gettingLemmas;
    private final SitesServiceImpl ssi;
    private final DataToParse dataToParse;
    private final MyReentrantLock myLock;

    private List<CompletableFuture<ResultIndexing>> resultList;
    private volatile boolean isLock = true;


    public String startIndexing() {

        if (isLock) {
            log.info("Running parsing");
            List<SiteEntity> sites = addDataToSitesEntity();
            resultList = new ArrayList<>();
            isLock = false;
            try {
                resultList = sites.
                        stream().map(site -> createCompletableFuture(site)).toList();
                return new ResultIndexing().getResults();
            } catch (Exception ex) {
                return new ResultIndexing("Ошибка индексации").getResults();
            }
        } else
            return new ResultIndexing("Идет индексирование").getResults();
    }

    public List<ResultIndexing> getResultFromFuture() {
        List<ResultIndexing> resultIndexingList = new ArrayList<>();

        resultList.forEach(f -> {
            try {
                resultIndexingList.add(f.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                isLock = true;
            }
        });
        return resultIndexingList;
    }

    private CompletableFuture<ResultIndexing> createCompletableFuture(SiteEntity site) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            WebLinkParser linkParser = new WebLinkParser(dataToParse, psi, ssi, gettingLemmas);
            linkParser.start(site);
        });
        return future.handle((res, ex) -> ex == null ?
                new ResultIndexing() :
                new ResultIndexing("Не удалось проиндексировать сайт : " + site.getName()));
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
