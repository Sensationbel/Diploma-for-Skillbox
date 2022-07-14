package by.bulavkin.searchEngine.contentService.stopIndexing;

import by.bulavkin.searchEngine.contentService.startIndexing.GettingLemmas;
import by.bulavkin.searchEngine.contentService.startIndexing.StartingIndexing;
import by.bulavkin.searchEngine.dataService.mySingletone.MyForkJoinPool;
import by.bulavkin.searchEngine.parsing.RecursiveWebLinkParser;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoppingIndexing {

    private final GettingLemmas gettingLemmas;
    private final WebLinkParser webLinkParser;



    public boolean stopIndexing() throws InterruptedException {
        ForkJoinPool pool = MyForkJoinPool.getMyForkJoinPool();
        boolean isStop = pool.isQuiescent();

        long taskCount = pool.getActiveThreadCount();
        log.info("task count before - > " + taskCount + ", isActiv: " + pool.isQuiescent());
        webLinkParser.setStop(true);
        gettingLemmas.setStop(true);
        Thread.sleep(3000);
        taskCount = pool.getActiveThreadCount();
        log.info("task count after - > " + taskCount + ", isActiv: " + pool.isQuiescent());
        return pool.isQuiescent();
    }

}
