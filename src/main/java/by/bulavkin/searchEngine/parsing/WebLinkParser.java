package by.bulavkin.searchEngine.parsing;

import by.bulavkin.searchEngine.contentService.startIndexing.GettingLemmas;
import by.bulavkin.searchEngine.dataService.mySingletone.MyForkJoinPool;
import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.entity.SiteEntity;
import by.bulavkin.searchEngine.entity.Status;
import by.bulavkin.searchEngine.dataService.implementation.PageServiceImp;
import by.bulavkin.searchEngine.dataService.implementation.SitesServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Getter
@Setter
@Log4j2
public class WebLinkParser {

    private final GettingLemmas gettingLemmas;
    private final DataToParse dataToParse;
    private final PageServiceImp psi;
    private final SitesServiceImpl ssi;
    private SiteEntity siteEntity;

    private final List<PageEntity> pageEntities;
    private final List<String> listIsVisit;

    private static int MAX_TREADS = Runtime.getRuntime().availableProcessors();
    private static String regex = "(https?|HTTPS?)://.+?/";

    private boolean stop = false;

    public void start(SiteEntity siteEntity) {
        this.siteEntity = siteEntity;
        try {
            log.info("Start parsing: " + siteEntity.getName());
            MyForkJoinPool.getMyForkJoinPool().invoke(new RecursiveWebLinkParser(parsingPage(siteEntity.getUrl()), this));
            psi.saveALL(pageEntities);
            log.info("Stop Parsing: " + siteEntity.getName());
        } catch (InterruptedException | IOException e) {
            log.error(e);
        }

    }

    public Set<String> parsingPage(String pageUrl) throws IOException, InterruptedException {

        Set<String> urls = new HashSet<>();
        if(stop){
            return urls;
        }

        Connection.Response response = getResponse(pageUrl);

        int statusCode = response.statusCode();
        changeStatusCode(pageUrl, response, statusCode);
        Document doc = response.parse();
        String contentCurrentURL = doc.html();
        createPageEntityFromUrl(pageUrl, statusCode, contentCurrentURL);

        for (Element element : doc.select("a")) {
            String currentUrl = element.attr("abs:href");
            if (isValidToVisit(currentUrl)) {
                getListIsVisit().add(currentUrl);
                urls.add(currentUrl);
            }
        }
        return urls;
    }

    private void changeStatusCode(String pageUrl, Connection.Response response, int statusCode) {
        if (statusCode != 200 && pageUrl.equals(siteEntity.getUrl())) {
            log.info(response.statusMessage() + " -> " + pageUrl);
            this.siteEntity.setStatus(Status.FILED);
            this.siteEntity.setLastError(response.statusMessage());
            ssi.save(siteEntity);
        } else if (pageUrl.equals(siteEntity.getUrl()) && !isVisit(pageUrl)) {
            this.siteEntity.setStatus(Status.INDEXED);
            ssi.save(siteEntity);
        }
    }

    @NotNull
    private Connection.Response getResponse(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl)
                .userAgent(dataToParse.getUserAgent())
//                .timeout(10000)
                .referrer(dataToParse.getReferrer())
                .ignoreHttpErrors(true)
                .execute();
    }

    private void createPageEntityFromUrl(String pageUrl, int statusCode, String contentCurrentURL) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPath(pageUrl.replaceAll(regex, "/"));
        pageEntity.setCode(statusCode);
        pageEntity.setContent(contentCurrentURL);
        pageEntity.setSite(siteEntity);
        pageEntities.add(pageEntity);
    }

    private boolean isValidToVisit(String currentUrl) {
        return currentUrl.startsWith(siteEntity.getUrl())
                && !isVisit(currentUrl)
                && !isFileUrl(currentUrl)
                && !currentUrl.contains("#")
                && !currentUrl.contains("?")
                && !currentUrl.endsWith("//");
    }

    private boolean isVisit(String currentUrl) {
        return getListIsVisit().contains(currentUrl);
    }

    private boolean isFileUrl(String currentUrl) {
        Pattern fileFilter =
                Pattern.compile(
                        ".*(\\.(css|js|bmp|gif|jpe?g|JPG|webp|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
                                + "|doc|docx|rm|smil|wmv|swf|wma|zip|rar|gz|xls|ppt|pptx|xlsx))$");
        return fileFilter.matcher(currentUrl).matches();
    }
}
