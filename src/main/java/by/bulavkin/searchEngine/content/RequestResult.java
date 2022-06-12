package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.service.PageServiceImp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@Getter
@Setter
public class RequestResult {

    private PageServiceImp psi;

    private String uri = "/path/to/page/";
    private String title;
    private String snippet;
    private float relevance;

    @Autowired
    public RequestResult(PageServiceImp psi) {
        this.psi = psi;
    }

    public void addResultList(List<Relevance> relevanceList){
        List<RequestResult> resultList = new ArrayList<>();

        relevanceList.forEach(relevance -> {
            RequestResult result = new RequestResult();
            PageEntity page = psi.findById(relevance.getPageId());
            result.setUri(uri + relevance.getPageId());
            title = getTitleFromPageEntity(page);
            result.setTitle(title);
            result.setRelevance(relevance.getRelRelevance());
            //snippet = getSnippetFromPageEntity(page);
            resultList.add(result);
        });

    }

//    private String getSnippetFromPageEntity(PageEntity page) {
//        Document doc = Jsoup.parse(page.getContent());
//        doc.getElementsContainingText()
//        return getSnippetFromPageEntity(page);
//    }

    private String getTitleFromPageEntity(PageEntity page) {
        Document doc = Jsoup.parse(page.getContent());
        return doc.title();
    }

}
