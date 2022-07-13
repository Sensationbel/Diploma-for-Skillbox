package by.bulavkin.searchEngine.content.search;

import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.service.implementation.PageServiceImp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Service
@NoArgsConstructor
@Getter
@Setter
public class RequestResult {

    private PageServiceImp psi;
    private Lemmatizer lemmatizer;

    private String uri = "/path/to/page/";
    private String title;
    private String snippet;
    private float relevance;

    @Autowired
    public RequestResult(PageServiceImp psi, Lemmatizer lemmatizer) {
        this.psi = psi;
        this.lemmatizer = lemmatizer;
    }
    public void addResultList(List<Relevance> relevanceList, String searchingRequest){
        List<RequestResult> resultList = new ArrayList<>();

        relevanceList.forEach(relevance -> {
            RequestResult result = new RequestResult();
            PageEntity page = psi.findById(relevance.getPageId());
            result.setUri(uri + relevance.getPageId());
            title = getTitleFromPageEntity(page);
            result.setTitle(title);
            result.setRelevance(relevance.getRelRelevance());
            snippet = getSnippet(page, searchingRequest);
            resultList.add(result);
        });

    }

    private String getSnippet(PageEntity page, String searchingRequest) {
        Document doc = Jsoup.parse(page.getContent());
        String[] textUrl = doc.text().trim().split("(\\s+)");
        String[] textRequest = searchingRequest.toLowerCase().split("(\\s+)");
        TreeSet<Integer> setIndexes = new TreeSet<>();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < textUrl.length; i++) {
            String text = textUrl[i];

            if (!textUrl[i].matches("[А-яЁё]+")) {
                continue;
            }

            assert lemmatizer.getLm() != null;
            int lastKey = 0;

            String lemmaUrl = lemmatizer.getLm().getNormalForms(text.toLowerCase()).get(0);

            for (String req : textRequest) {
                String lReq = lemmatizer.getLm().getNormalForms(req).get(0);

                if (lReq.equals(lemmaUrl)) {
                    setIndexes.add(i);
                    if (!setIndexes.isEmpty()) {
                        lastKey = setIndexes.last();
                    }
                    if (lastKey == (i - 1)) {
                        String lastValue = textUrl[lastKey].replaceAll("</b>", "");
                        textUrl[lastKey] = lastValue;
                        textUrl[i] = text + "</b>";
                    } else textUrl[i] = "<b>" + text + "</b>";
                }
            }
        }

        for (int i = setIndexes.first(); i <= setIndexes.last(); i++) {
            result.append(textUrl[i]).append(" ");
        }
        return result.toString();
    }

    private String getTitleFromPageEntity(PageEntity page) {
        Document doc = Jsoup.parse(page.getContent());
        return doc.title();
    }

}
