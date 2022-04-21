package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.repositoties.DataFromUrlRepository;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import by.bulavkin.searchEngine.service.WebLinkParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final DataFromUrlRepository dfur;
    private final FieldRepository fr;

    @NonNull
    private WebLinkParser wlp;

    @GetMapping("/url")
    public String getUrl() {
//        wlp.start();
//        dfur.saveAll(wlp.getListDfu());
//        fr.insertFieldEntity(1, "title", "title", 1.0F);
//        fr.insertFieldEntity(2, "body", "body", 0.8F);
        return "Парсинг завершен";
    }
}
