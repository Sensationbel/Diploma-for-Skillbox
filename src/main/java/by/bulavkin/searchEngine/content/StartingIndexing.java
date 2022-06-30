package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.*;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.parsing.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartingIndexing {

    private final PageServiceImp psi;

    private final IndexServiceImp isi;
    private final SitesServiceImpl ssi;

    private final DataToParse dataToParse;

    private final GettingLemmas gettingLemmas;

    public void startParsingSites(){
        List<SiteEntity> sites = addDataToSitesEntity();

        for (SiteEntity s : sites) {
            Runnable task = () -> {
                WebLinkParser linkParser = new WebLinkParser(dataToParse, psi, ssi, gettingLemmas);
                linkParser.start(s);
            };
            Thread thread = new Thread(task, s.getName());
            thread.start();
        }
    }

     public List<SiteEntity> addDataToSitesEntity(){
        List<SiteEntity> list = new ArrayList<>();

        dataToParse.getSites().forEach(s ->{
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
