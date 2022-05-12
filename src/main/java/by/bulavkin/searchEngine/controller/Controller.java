package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Service
public class Controller {

    private final Content c;

    @GetMapping("/index")
    public String saveIndex() throws IOException, InterruptedException {
        c.startAddContentToDatabase();
        return "Индексирование завершено";
    }
}
