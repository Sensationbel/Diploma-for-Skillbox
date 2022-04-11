package by.bulavkin.searchEngine.service;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@AllArgsConstructor
public class RecursiveWebLinkParser extends RecursiveAction {

    private Set<String> urls;
    private WebLinkParser wlp;

    @Override
    protected void compute() {
        if (urls == null || urls.isEmpty()) {
            if (getPool().getQueuedTaskCount() == 0) {
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
                System.err.println(recUrl);
                ;
            }
        });
        taskList.forEach(ForkJoinTask::join);
    }
}
