package by.bulavkin.searchEngine.content;

import lombok.*;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor()
public class ContentFromTitleAndBody {

    private String titleContent;
    private String bodyContent;

    @Synchronized
    public List<String[]> addDataToListContentFromTitleAndBody(Document doc) {
       List<String[]> ctbList = new ArrayList<>();
        ctbList.add(new String[]{doc.select("title").text(), doc.body().text()});
      return ctbList;
    }
}
