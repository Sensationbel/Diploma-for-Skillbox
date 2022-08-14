package by.bulavkin.searchEngine.services.contentServices.startIndexing;

import by.bulavkin.searchEngine.dto.errors.ResultIndexing;
import by.bulavkin.searchEngine.dto.errors.ResultIndexingErrors;
import by.bulavkin.searchEngine.dto.errors.ResultIndexingNotErrors;
import by.bulavkin.searchEngine.services.dataService.implementation.SitesServiceImpl;
import by.bulavkin.searchEngine.config.InstanceForkJoinPool;
import by.bulavkin.searchEngine.model.SiteEntity;
import by.bulavkin.searchEngine.config.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.services.dataService.interfeises.SitesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class StartingIndexingService {

    private final WebLinkParser webLinkParser;
    private final GettingLemmasService gettingLemmas;
    private final SitesService ssi;
    private final DataToParse dataToParse;


    private List<CompletableFuture<Void>> futureList;
    private volatile boolean isLock = true;

    public ResultIndexing startIndexing() {

        if (isLock) {
            log.info("Running parsing");
            List<SiteEntity> sites = addDataToSitesEntity();
            futureList = new ArrayList<>();
            isLock = false;
            try {
                futureList = sites.
                        stream().map(this::createCompletableFuture).toList();
                return new ResultIndexingNotErrors(true);
            } catch (Exception ex) {
                return new ResultIndexingErrors("Ошибка индексации");
            }
        } else
            return new ResultIndexingErrors("Идет индексирование");
    }

    public void getResultFromFuture() {
        futureList.forEach(CompletableFuture::join);
        isLock = true;

    }

    private CompletableFuture<Void> createCompletableFuture(SiteEntity site) {
        return CompletableFuture.runAsync(() -> {
            webLinkParser.start(site);
        }, InstanceForkJoinPool.getMyForkJoinPool()).
                thenRunAsync(() -> gettingLemmas.
                        startAddContentToDatabase(site),
                        InstanceForkJoinPool.getMyForkJoinPool());
    }

    private List<SiteEntity> addDataToSitesEntity() {
        List<SiteEntity> list = new ArrayList<>();

        dataToParse.getSites().forEach(s -> {
            SiteEntity site = ssi.findByUrl(s.getUrl());
            if(site == null){
                site = new SiteEntity();
                site.setStatus(SiteEntity.Status.INDEXING);
                site.setStatusTime(Timestamp.valueOf(LocalDateTime.now()));
                site.setLastError(null);
                site.setUrl(s.getUrl());
                site.setName(s.getName());
                log.info("Adding new site: " + site.getName());
            } else{
                site.setStatus(SiteEntity.Status.INDEXING);
                log.info("Adding site: " + site.getName());
            }
            list.add(site);
        });
        return ssi.saveALL(list);
    }
}
