package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Component
public class ContentFromLemmas {

    private ContentFromTitleAndBody cftab = new ContentFromTitleAndBody();
    private Lemmatizer lemmatizer = new Lemmatizer();

    private Map<String, Integer> lemmaFromTitle = new HashMap<>();
    private Map<String, Integer> LemmaFromBody = new HashMap<>();
    private String url;


    public void addDataToNumberOfLemm(Document doc, String url){
        this.url = url;
        cftab.addDataToListContentFromTitleAndBody(doc).forEach(c -> {
            this.lemmaFromTitle = lemmatizer.lemmatization(c[0]);
            this.LemmaFromBody = lemmatizer.lemmatization(c[1]);
        });
    }
}
