package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.Content;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.LemmaService;
import by.bulavkin.searchEngine.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Service
public class Controller {

    private final PageService ps;
    private final LemmaService ls;
    private final WebLinkParser wlp;
    private final Content c;

    @GetMapping("/url")
    public String getUrl() throws IOException, InterruptedException {
        wlp.start();
        ps.saveALL(wlp.getListPE());
        return "Парсинг завершен";
    }

    @GetMapping("/index")
    public String saveIndex(){
        c.startAddContentToDatabase();
        return "Индексирование завершено";
    }
}
