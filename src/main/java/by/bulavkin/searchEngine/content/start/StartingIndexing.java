package by.bulavkin.searchEngine.content.start;

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
import java.util.concurrent.Phaser;

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

    private boolean status = false;

    public void startIndexing() {
            log.info("Running parsing");
            startParsingSites();
            setStatus(true);
            log.info("Stop parsing");
    }

    private void startParsingSites() {
        List<SiteEntity> sites = addDataToSitesEntity();
        Phaser phaser = new Phaser();
        phaser.register();
        for (SiteEntity s : sites) {
            phaser.register();
            Runnable task = () -> {
                WebLinkParser linkParser = new WebLinkParser(dataToParse, psi, ssi, gettingLemmas);
                phaser.arriveAndAwaitAdvance();
                linkParser.start(s);
            };
            Thread thread = new Thread(task, s.getName());
            threads.add(thread);
            thread.start();
        }
        try {
            Thread.sleep(500);
            phaser.arriveAndAwaitAdvance();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

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
