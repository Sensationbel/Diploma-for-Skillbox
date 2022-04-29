package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.ContentFromLemmas;
import by.bulavkin.searchEngine.repositoties.DataFromUrlRepository;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Service
public class Controller {

    private final DataFromUrlRepository dfur;
    private final FieldRepository fr;
    private final WebLinkParser wlp;

    @Autowired
    private final ContentFromLemmas cfl;

    @GetMapping("/url")
    public String getUrl() throws IOException, InterruptedException {
        wlp.parsingPage("https://www.lutherancathedral.ru/");
        dfur.saveAll(wlp.getListDfu());
//        fr.insertFieldEntity(1, "title", "title", 1.0F);
//        fr.insertFieldEntity(2, "body", "body", 0.8F);
        return "Парсинг завершен";
    }

    @GetMapping("/lemma")
    public Map<String, Integer> getLemmas(){

        return wlp.getCfl().getLemmaFromTitle();
    }
}
