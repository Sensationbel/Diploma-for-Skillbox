package by.bulavkin.searchEngine.parsing;

import by.bulavkin.searchEngine.content.ContentFromLemmas;
import by.bulavkin.searchEngine.entity.PageEntity;
import lombok.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;


@Service
//@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class WebLinkParser {

    private List<PageEntity> listPE = new ArrayList<>();
    private volatile List<String> listIsVisit = new ArrayList<>();

    private String link, userAgent, referrer;
    private static int MAX_TREADS = Runtime.getRuntime().availableProcessors() * 2;

    private ContentFromLemmas cfl = new ContentFromLemmas();

    public void start() {
        try {
            try {
                System.out.println("FJK start");
                new ForkJoinPool(MAX_TREADS).invoke(new RecursiveWebLinkParser(parsingPage(link), this));
                System.out.println("FJK stop");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> parsingPage(String pageUrl) throws IOException, InterruptedException {
        Thread.sleep(1500);
        System.out.println(pageUrl);
        Set<String> urls = new HashSet<>();
        Connection.Response response = Jsoup.connect(pageUrl)
                .userAgent(userAgent)
                .timeout(7000)
                .referrer(referrer)
                .ignoreHttpErrors(true)
                .execute();
        int statusCode = response.statusCode();
        Document doc = response.parse();
        String contentCurrentURL = doc.html();
        cfl.addDataToNumberOfLemm(doc);
        createDataFromUrl(pageUrl, statusCode, contentCurrentURL);
        for (Element element : doc.select("a")) {
            String currentUrl = element.attr("abs:href");
            if (isValidToVisit(currentUrl)) {
                getListIsVisit().add(currentUrl);
                urls.add(currentUrl);
            }
        }
        return urls;
    }

    private void createDataFromUrl(String pageUrl, int statusCode, String contentCurrentURL) {
        PageEntity dfu = new PageEntity();
        dfu.setPath(pageUrl.replaceAll(link, "/"));
        dfu.setCode(statusCode);
        dfu.setContent(contentCurrentURL);
        addListDfu(dfu);
    }

    private void addListDfu(PageEntity dataFromUrl) {
        listPE.add(dataFromUrl);
    }

    private boolean isValidToVisit(String currentUrl) {
        return currentUrl.startsWith(link)
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
                                + "|doc|rm|smil|wmv|swf|wma|zip|rar|gz|xls))$");
        return fileFilter.matcher(currentUrl).matches();
    }
}
