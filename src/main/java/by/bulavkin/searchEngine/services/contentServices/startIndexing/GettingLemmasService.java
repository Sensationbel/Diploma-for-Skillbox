package by.bulavkin.searchEngine.services.contentServices.startIndexing;

import by.bulavkin.searchEngine.model.*;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.services.dataService.interfeises.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class GettingLemmasService {

    private final PageService ps;
    private final SitesService ss;
    private final FieldService fs;
    private final LemmaService ls;
    private final IndexService is;
    private final Lemmatizer lemmatizer;

    private final ArrayList<IndexEntity> indexes;
    private final ArrayList<LemmaEntity> lemmaEntities;

    private List<FieldEntity> fields;

    private SiteEntity site;
    private boolean stop = false;

    public void startAddContentToDatabase(SiteEntity site) {
        this.site = site;
        log.info("Start add content for --->" + site.getName());
        fields = new ArrayList<>(fs.findAll());
        log.info("get fields");
        ls.deleteAllBySiteId(site.getId());
        log.info("delete lemmas ");
        ArrayList<PageEntity> pageEntities = new ArrayList<>(ps.findAllBySiteId(site.getId()));
        log.info("find site by Id: " + site.getId());
        pageEntities.
                forEach(p -> {
                    log.info("start parsing page Id: " + p.getId());
                    if (stop) {
                        log.info("stop parsing");
                        site.setStatus(SiteEntity.Status.FILED);
                        site.setLastError("Остановлено пользователем");
                        ss.save(site);
                        return;
                    } else {
                        log.info("page Id: " + p.getId() + " before normalizeContent ");
                        normalizeContent(p);
                        log.info("page Id: " + p.getId() + " after normalizeContent ");
                    }
                });

        log.info("start to add to table index");
        is.saveAll(indexes);
        log.info("Stop add content for --->" + site.getName());
    }

    public void normalizeContent(PageEntity page) {
        if (page.getCode() != 200) {
            return;
        }
        log.info("page Id: " + page.getId() + " in normalizeContent ");
        Document doc = Jsoup.parse(page.getContent());
        fields.forEach(field -> {
            Elements contentQuery = doc.select(field.getSelector());
            String normalizeContent = contentQuery.text().replaceAll("[^ЁёА-я\s]", " ").trim();
            Map<String, Integer> lemmas = new HashMap<>(lemmatizer.getMapWithLemmas(normalizeContent));
            log.info("field " + field.getId());
            log.info("page Id: " + page.getId() + " before lemmatizer");
            lemmaEntities.addAll(addLemma(lemmas, page));
            log.info("page Id: " + page.getId() + " after lemmatizer");

            rankCalculation(page, lemmas, field.getWeight());
            log.info("page Id: " + page.getId() + " after ranCCalculate");

        });

            log.info("normalize content stop");

    }

    private List<LemmaEntity> addLemma(Map<String, Integer> lemmas, PageEntity page) {
        ArrayList<LemmaEntity> listForSave = new ArrayList<>();
        log.info("page Id: " + page.getId() + " in addLemma");
        lemmas.keySet().forEach(lemma -> {
            LemmaEntity lemmaEntity = new LemmaEntity();

//            LemmaEntity lemmaEntity = getLemmaEntity(lemma, page.getId());
//            if (lemmaEntity.isEmpty()) {
            lemmaEntity.setLemma(lemma);
            lemmaEntity.setFrequency(1);
//                lemmaEntity.setPageId(page.getId());
            lemmaEntity.setSite(page.getSite());
//            } else {
//                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
//            }
            listForSave.add(lemmaEntity);
        });
        if(page.getId() == 79){
            listForSave.forEach(lemmaEntity -> {
                System.out.println(lemmaEntity.getId() +
                        "  " + lemmaEntity.getLemma() +
                        "  " + lemmaEntity.getFrequency()  + "\n");
            });
        }
        log.info("page Id: " + page.getId() + " after addLemma");
        return ls.saveAll(listForSave);
    }

    public void rankCalculation(PageEntity page, Map<String, Integer> lemmas, Float weight) {
        int pageId = page.getId();

        lemmas.forEach((k, v) -> {
            LemmaEntity lemma = getLemmaEntity(k);
            IndexEntity indexEntity = getIndexEntity(lemma.getId(), pageId);

            if (indexEntity.isEmpty()) {
                indexEntity.setPageEntity(page);
                indexEntity.setLemmaEntity(lemma);
                indexEntity.setRank(v * weight);
                indexes.add(indexEntity);
            } else {
                indexEntity.setRank(indexEntity.getRank() + (v * weight));
            }
        });
    }

    private LemmaEntity getLemmaEntity(String lemma) {
        return lemmaEntities.
                stream().
                filter(lemmaEntity -> lemmaEntity.getLemma().equals(lemma)).
                findFirst().orElseThrow();
    }

    private IndexEntity getIndexEntity(int lemmaId, int pageId) {
        return indexes.
                stream().
                filter(i -> (i.getLemmaEntity().getId() == lemmaId && i.getPageEntity().getId() == pageId)).
                findFirst().
                orElse(new IndexEntity());
    }
}
