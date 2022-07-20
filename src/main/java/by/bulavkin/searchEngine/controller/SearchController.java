package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.contentService.search.ProcessingSearch;
import by.bulavkin.searchEngine.contentService.search.Relevance;
import by.bulavkin.searchEngine.contentService.startIndexing.StartingIndexing;
import by.bulavkin.searchEngine.contentService.statistics.ResultIndexing;
import by.bulavkin.searchEngine.contentService.statistics.Statistics;
import by.bulavkin.searchEngine.contentService.stopIndexing.StoppingIndexing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public record SearchController(StoppingIndexing stop,
                               StartingIndexing start,
                               ProcessingSearch request,
                               Relevance relevance,
                               Statistics statistic) {

//    @GetMapping("/admin")
//    @ResponseBody
//    public String showSite() {
//
//       return "index";
//    }

    /**
     * TODO: Метод запускает полную индексацию всех сайтов или полную
     * переиндексацию, если они уже проиндексированы.
     * 19
     * Если в настоящий момент индексация или переиндексация уже
     * запущена, метод возвращает соответствующее сообщение об ошибке.
     * Параметры:
     * Метод без параметров
     * Формат ответа в случае успеха:
     * {
     * 'result': true
     * }
     * Формат ответа в случае ошибки:
     * {
     * 'result': false,
     * 'error': "Индексация уже запущена"
     * }
     */

    @GetMapping("/startIndexing")
    @ResponseBody
    public String startIndexing() {
        return start.startIndexing();
    }

    /**
     * TODO: Метод останавливает текущий процесс индексации
     * (переиндексации). Если в настоящий момент индексация или
     * переиндексация не происходит, метод возвращает соответствующее
     * сообщение об ошибке.
     * Параметры:
     * Метод без параметров.
     * Формат ответа в случае успеха:
     * {
     * 'result': true
     * }
     * Формат ответа в случае ошибки:
     * {
     * 'result': false,
     * 'error': "Индексация не запущена"
     * }
     */

    @GetMapping("/stopIndexing")
    public Map<String, Boolean> stopIndexing() throws InterruptedException {
        boolean res = stop.stopIndexing();
        return Map.of("result", res);
    }

    @GetMapping("/api/search")
    public List<Relevance> searchURL() {
        request.normaliseRequest("Региональная естественнонаучная конференция");
        return relevance.addRelevanceList(request.addIndexEntitiesByLemma());
    }

    /**
     * TODO: Добавление или обновление отдельной страницы — POST
     * /api/indexPage
     * Метод добавляет в индекс или обновляет отдельную страницу,
     * адрес которой передан в параметре.
     * Если адрес страницы передан неверно, метод должен вернуть
     * соответствующую ошибку.
     * Параметры:
     * ● url — адрес страницы, которую нужно переиндексировать.
     * Формат ответа в случае успеха:
     * {
     * 'result': true
     * }
     * Формат ответа в случае ошибки:
     * {
     * 'result': false,
     * 'error': "Данная страница находится за пределами
     * сайтов,
     * указанных в конфигурационном файле"
     * }
     */

    @PostMapping("/api/indexPage")
    @ResponseBody
    public boolean indexPages(@RequestParam String url) {
        return true;
    }

    /**
     * TODO: Статистика — GET /api/statistics
     * Метод возвращает статистику и другую служебную информацию о
     * состоянии поисковых индексов и самого движка.
     * Параметры:
     * Метод без параметров.
     * Формат ответа:
     * {
     * 'result': true,
     * 'statistics': {
     * "total": {
     * "sites": 10,
     * "pages": 436423,
     * "lemmas": 5127891,
     * "isIndexing": true
     * },
     * "detailed": [
     * {
     * "url": "http://www.site.com",
     * "name": "Имя сайта",
     * "status": "INDEXED",
     * "statusTime": 1600160357,
     * "error": "Ошибка индексации: главная
     * страница сайта недоступна",
     * "pages": 5764,
     * "lemmas": 321115
     * },
     * ...
     * ]
     * }
     */

    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<?> statistics() {
//        try {
//            return ResponseEntity.ok().body(statistic.getALLStatistics());
//        } catch (Exception e) {
//            return ResponseEntity.ok().body(new ResultIndexing("При построении статистики произошла ошибка"
//                    + System.lineSeparator()
//                    + e.getMessage()));
//        }
        String text = "{\"result\": true," +
                " \"statistics\": " +
                "{\"total\": " +
                "{\"sites\": 1, \"pages\": 89, \"lemmas\": 2677, \"isIndexing\": true}, " +
                "\"detailed\": [{\"url\": " +
                "\"https://nikoartgallery.com/\"," +
                " \"name\": \"ArtGallery\"," +
                " \"status\": \"INDEXED\"," +
                " \"statusTime\": \"2022-07-16 15:47:05.0\"," +
                " \"error\": \"null\"," +
                " \"pages\": 89," +
                " \"lemmas\": 2677}]}}";
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        return ResponseEntity.ok().body(builder);
    }
}
