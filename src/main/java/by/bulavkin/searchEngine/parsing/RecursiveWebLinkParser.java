package by.bulavkin.searchEngine.parsing;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Service
@AllArgsConstructor
@Log4j2
public class RecursiveWebLinkParser extends RecursiveAction {

    private Set<String> urls;
    private WebLinkParser wlp;


    @Override
    protected void compute() {

        if (urls == null || urls.isEmpty()) {
            if(getPool().getQueuedTaskCount() == 0){
                getPool().shutdown();
            }
            return;
        }
        List<RecursiveWebLinkParser> taskList = new ArrayList<>();
        urls.forEach(recUrl -> {
            try {
                RecursiveWebLinkParser task = new RecursiveWebLinkParser(wlp.parsingPage(recUrl), wlp);
                task.fork();
                taskList.add(task);
            } catch (IOException | InterruptedException e) {
                log.error(e);
                log.info(recUrl);
            }
        });
        taskList.forEach(ForkJoinTask::join);
    }
}
