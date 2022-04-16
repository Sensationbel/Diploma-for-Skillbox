package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.entity.DataFromUrlRepository;
import by.bulavkin.searchEngine.service.WebLinkParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Controller {


    private final DataFromUrlRepository lr;

    @NonNull
    private WebLinkParser wlp;

    @GetMapping("/url")
    public String getUrl() {
        wlp.start();
        lr.saveAll(wlp.getListDfu());
        StringBuilder sb = new StringBuilder();
        wlp.getListDfu().forEach(dfu -> sb.append(dfu.getPath()));
        return sb.toString();
    }
}
