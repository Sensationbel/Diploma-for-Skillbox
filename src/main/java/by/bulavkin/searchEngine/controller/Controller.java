package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.entity.DataFromUrlRepository;
import by.bulavkin.searchEngine.service.WebLinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    @Autowired
    DataFromUrlRepository lr;

    @Autowired
    WebLinkParser wlp;

    @GetMapping("/url")
    public String getUrl(){
        wlp.start();
        lr.saveAll(wlp.getListDfu());
        StringBuilder sb = new StringBuilder();
        wlp.getListDfu().forEach(dfu -> sb.append(dfu.getPath()));
        return sb.toString();
    }
}
