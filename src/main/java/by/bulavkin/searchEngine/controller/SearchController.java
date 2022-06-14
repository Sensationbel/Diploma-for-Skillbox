package by.bulavkin.searchEngine.controller;

import by.bulavkin.searchEngine.content.Content;
import by.bulavkin.searchEngine.content.Relevance;
import by.bulavkin.searchEngine.content.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
//@RequiredArgsConstructor
@Service
public record SearchController(Content content, Request request, Relevance relevance) {

    @GetMapping("/")
//    @ResponseBody
    public StringBuilder startAppl(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sites\": 1}\n").append("{\"pages\": 34}\n").append("{\"lemmas\": 234}\n");
        builder.append(getStatistic());
        return builder;
    }
    /**
     TODO: Метод запускает полную индексацию всех сайтов или полную
     переиндексацию, если они уже проиндексированы.
     19
     Если в настоящий момент индексация или переиндексация уже
     запущена, метод возвращает соответствующее сообщение об ошибке.
     Параметры:
     Метод без параметров
     Формат ответа в случае успеха:
     {
     'result': true
     }
     Формат ответа в случае ошибки:
     {
     'result': false,
     'error': "Индексация уже запущена"
     }
     */

    @GetMapping("/api/start_indexing")
    public Map<String, Boolean> startIndexing() {
        //content.startAddContentToDatabase();
        return Map.of("result", true);
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

    @GetMapping("/api/stop_indexing")
    public Map<String, Boolean> stopIndexing() {
        //content.startAddContentToDatabase();
        return Map.of("result", true);
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
//    @ResponseBody
    public boolean indexPages(@RequestParam String url){
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

    @GetMapping("/api/statistics")
    public String getStatistic(){
        return "\"result\": true," +
                "\"statistics\": {" +
                "\"total\": {" +
                "\"sites\": 10," +
                "\"pages\": 436423," +
                "\"lemmas\": 5127891," +
                "\"isIndexing\": true" +
                "}," +
                "\"detailed\": [" +
                "{" +
                "\"url\": \"http://www.site.com\"," +
                "\"name\": \"Имя сайта\"," +
                "\"status\": \"INDEXED\"," +
                "\"statusTime\": 1600160357," +
                "\"error\": \"Ошибка индексации: главная" +
                "страница сайта недоступна\"," +
                "\"pages\": 5764," +
                "\"lemmas\": 321115" +
                "}," +
                "]" +
                "}";
    }
}
