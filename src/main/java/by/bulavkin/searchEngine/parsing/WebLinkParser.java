package by.bulavkin.searchEngine.parsing;

import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.entity.Sites;
import by.bulavkin.searchEngine.entity.Status;
import by.bulavkin.searchEngine.service.SitesServiceImpl;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
//@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@Log4j2
public class WebLinkParser {

    private SitesServiceImpl ssi;

    private volatile List<PageEntity> pageEntities = new ArrayList<>();
    private volatile List<String> listIsVisit = new ArrayList<>();


    private DataToParse dataToParse;
    private List<Sites> sitesList;
    private static int MAX_TREADS = Runtime.getRuntime().availableProcessors();
    private static String regex = "(https?|HTTPS?)://.+?/";

    public void start(List<Sites> sites, DataToParse dataToParse, SitesServiceImpl ssi) {
        this.sitesList = sites;
        this.dataToParse = dataToParse;
        this.ssi = ssi;

        changeSiteStatus(Status.INDEXED, sites);
        log.info("Pars start");
        new ForkJoinPool(MAX_TREADS).invoke(new RecursiveWebLinkParser(getUrlSet(), this));
        log.info("Pars stop");
        changeSiteStatus(Status.INDEXING, sites);
    }

    private void changeSiteStatus(Status status, List<Sites> sites) {
        sites.forEach(s -> {
            s.setStatus(status);
            ssi.save(s);
        });
    }

    public Set<String> parsingPage(String pageUrl) throws IOException, InterruptedException {
        Thread.sleep(500);
        Set<String> urls = new HashSet<>();

        Connection.Response response = Jsoup.connect(pageUrl)
                .userAgent(dataToParse.getUserAgent())
                .timeout(3000)
                .referrer(dataToParse.getReferrer())
                .ignoreHttpErrors(true)
                .execute();

        int statusCode = response.statusCode();
        Document doc = response.parse();
        String contentCurrentURL = doc.html();
        createDataFromUrl(pageUrl, statusCode, contentCurrentURL);

        for (Element element : doc.select("a")) {
            String currentUrl = element.attr("abs:href");
            if (isValidToVisit(pageUrl, currentUrl)) {
                getListIsVisit().add(currentUrl);
                urls.add(currentUrl);
            }
        }
        return urls;
    }

    private void createDataFromUrl(String pageUrl, int statusCode, String contentCurrentURL) {
        PageEntity pageEntity = new PageEntity();
        Sites site = getSiteByPageurl(pageUrl);


        if (site.isEmpty()){
            log.info(pageUrl + " not found!");
            return;
        }
        pageEntity.setPath(pageUrl.replaceAll(regex, "/"));
        pageEntity.setCode(statusCode);
        pageEntity.setContent(contentCurrentURL);
        pageEntity.setSite(getSiteByPageurl(pageUrl));
        pageEntities.add(pageEntity);
    }

    private Sites getSiteByPageurl(String pageUrl) {
        return sitesList.
                stream().
                filter(s -> pageUrl.startsWith(s.getUrl())).
                findFirst().
                orElse(new Sites());
    }

    private boolean isValidToVisit(String pageUrl, String currentUrl) {
        return currentUrl.startsWith(pageUrl)
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

    private Set<String> getUrlSet() {
        Set<String> urlSet = sitesList.
                stream().
                map(s -> s.getUrl()).
                collect(Collectors.toSet());
        return urlSet;
    }
}
