package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.ContentFromLemmas;
import by.bulavkin.searchEngine.repositoties.PageRepository;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.LemmaService;
import by.bulavkin.searchEngine.service.PageService;
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

    private final PageService ps;
    private final LemmaService ls;
    private final WebLinkParser wlp;

    @GetMapping("/url")
    public String getUrl() throws IOException, InterruptedException {
        wlp.parsingPage("https://www.lutherancathedral.ru/");
        ps.saveALL(wlp.getListPE());
        return "Парсинг завершен";
    }

    @GetMapping("/lemma")
    public Map<String, Integer> getLemmas(){
        ls.saveLemmaEntity(wlp.getCfl().getLemmaFromTitle());
        ls.saveLemmaEntity(wlp.getCfl().getLemmaFromBody());
        return wlp.getCfl().getLemmaFromBody();
    }
}
