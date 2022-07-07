package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.search.ProcessingSearch;
import by.bulavkin.searchEngine.content.search.Relevance;
import by.bulavkin.searchEngine.content.start.StartingIndexing;
import by.bulavkin.searchEngine.content.statistics.ResultIndexing;
import by.bulavkin.searchEngine.content.statistics.Statistics;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Service
@Slf4j
public record SearchController(StartingIndexing startingIndexing,
                               ProcessingSearch request,
                               Relevance relevance,
                               Statistics statistic) {

//    @GetMapping("/")
////    @ResponseBody
//    public String showSite() {
//       statistics();
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
    public ResponseEntity<?> startIndexing() {
        log.info("startIndexing -> " + Thread.currentThread().getName());
//        while ()
//        ResponseEntity.
//                ok().
//                body(new ResultIndexing("Идет индексирование"));

        return ResponseEntity.
                ok().
                body(startingIndexing.startIndexing());
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
    public Map<String, Boolean> stopIndexing() {
        log.info("stopIndexing -> " + Thread.currentThread().getName());
        //content.startAddContentToDatabase();
        return Map.of("result", false);
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
        return ResponseEntity.ok().body(statistic.getStatistics());
    }
}
