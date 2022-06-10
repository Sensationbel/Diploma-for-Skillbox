package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.Content;
import by.bulavkin.searchEngine.content.Relevance;
import by.bulavkin.searchEngine.content.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@Service
public record Controller(Content content, Request request, Relevance relevance) {

    @GetMapping("/index")
    public String saveIndex() {
        content.startAddContentToDatabase();
        return "Индексирование завершено";
    }

    @GetMapping("/search")
    public List<Relevance> searchURL() {
        request.normaliseRequest("Региональная естественнонаучная конференция");
        return relevance.addRelevanceList(request.addIndexEntitiesByLemma());
    }
}
