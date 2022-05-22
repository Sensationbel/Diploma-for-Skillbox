package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.Content;
import by.bulavkin.searchEngine.content.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Service
public class Controller {

    private final Content c;
    private final Request r;

    @GetMapping("/index")
    public String saveIndex(){
        c.startAddContentToDatabase();
        return "Индексирование завершено";
    }

    @GetMapping("/search")
    public String searchURL(){
        r.normaliseRequest("Состоялся невидимый ввод");
        r.addIndexEntities();
        return "Поиск завершен";
    }
}
