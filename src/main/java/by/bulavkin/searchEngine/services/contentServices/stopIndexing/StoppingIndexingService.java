package by.bulavkin.searchEngine.services.contentServices.stopIndexing;

import by.bulavkin.searchEngine.services.contentServices.startIndexing.GettingLemmasService;
import by.bulavkin.searchEngine.config.InstanceForkJoinPool;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoppingIndexingService {

    private final GettingLemmasService gettingLemmas;
    private final WebLinkParser webLinkParser;



    public boolean stopIndexing() throws InterruptedException {
        ForkJoinPool pool = InstanceForkJoinPool.getMyForkJoinPool();
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
