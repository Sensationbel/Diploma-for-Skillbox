package by.bulavkin.searchEngine.content.start;

import by.bulavkin.searchEngine.content.statistics.ResultIndexing;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.entity.Status;
import by.bulavkin.searchEngine.parsing.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.IndexServiceImp;
import by.bulavkin.searchEngine.service.PageServiceImp;
import by.bulavkin.searchEngine.service.SitesServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class StartingIndexing {

    private final PageServiceImp psi;

    private final IndexServiceImp isi;
    private final SitesServiceImpl ssi;

    private final DataToParse dataToParse;

    private final GettingLemmas gettingLemmas;

    private final List<Thread> threads;


    public Object startIndexing() {
        log.info("Running parsing");
        log.info("controller -> " + Thread.currentThread().getName());
        List<SiteEntity> sites = addDataToSitesEntity();

        List<CompletableFuture<String>> resultList = sites.
                stream().map(site -> startParsingSites(site)).toList();
        Object resultPars = Stream.of(resultList.toArray(new CompletableFuture[0])).
                map(CompletableFuture::join).findFirst().get();
        log.info("Stop parsing with resalt: " + resultPars.toString());
        return resultPars;
    }

    private CompletableFuture<String> startParsingSites(SiteEntity site) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    WebLinkParser linkParser = new WebLinkParser(dataToParse, psi, ssi, gettingLemmas);
                    linkParser.start(site);
                });
        log.info("indexing -> " + Thread.currentThread().isAlive());

        return future.handle((res, ex) -> ex == null ?
                        new ResultIndexing().getResult() :
                        new ResultIndexing("Не удалось проиндексиловать сайт : " + site.getName()).getResult());
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
