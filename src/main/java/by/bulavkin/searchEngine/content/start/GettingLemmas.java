package by.bulavkin.searchEngine.content.start;

import by.bulavkin.searchEngine.entity.*;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.service.implementation.FieldServiceIml;
import by.bulavkin.searchEngine.service.implementation.IndexServiceImp;
import by.bulavkin.searchEngine.service.implementation.LemmaServiceImp;
import by.bulavkin.searchEngine.service.implementation.PageServiceImp;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GettingLemmas {

    private final PageServiceImp psi;

    private final FieldServiceIml fsi;

    private final LemmaServiceImp lsi;

    private final IndexServiceImp isi;

    private final Lemmatizer lemmatizer;

    private final ArrayList<IndexEntity> indexes;

    private final ArrayList<LemmaEntity> lemmaEntities;

    @Autowired(required = false)
    private List<FieldEntity> fields;


    private SiteEntity site;

    public void startAddContentToDatabase(SiteEntity site) {
        this.site = site;
        log.info("Start add content for --->" + site.getName());
        fields = fsi.findAll();
        ArrayList<PageEntity> pageEntities = new ArrayList<>(psi.findAllBySiteId(site.getId()));
        pageEntities.
                forEach(this::normalizeContent);
        isi.saveAll(indexes);
    }

    public void normalizeContent(PageEntity page) {
        if (page.getCode() != 200) {
            return;
        }
        Document doc = Jsoup.parse(page.getContent());
        fields.forEach(field -> {
            Elements contentQuery = doc.select(field.getSelector());
            String normalizeContent = contentQuery.text().replaceAll("[^ЁёА-я\s]", " ").trim();
            Map<String, Integer> lemmas = new HashMap<>(lemmatizer.getMapWithLemmas(normalizeContent));
            lemmaEntities.addAll(addLemma(lemmas, page));
            rankCalculation(page, lemmas, field.getWeight());
        });
    }

    private List<LemmaEntity> addLemma(Map<String, Integer> lemmas, PageEntity page) {
        ArrayList<LemmaEntity> listForSave = new ArrayList<>();
        lemmas.keySet().forEach(lemma -> {
            LemmaEntity lemmaEntity = getLemmaEntity(lemma, page.getId());
            if (lemmaEntity.isEmpty()) {
                lemmaEntity.setLemma(lemma);
                lemmaEntity.setFrequency(1);
//                lemmaEntity.setPageId(page.getId());
                lemmaEntity.setSite(page.getSite());
            } else {
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
            }
            listForSave.add(lemmaEntity);
        });
        return lsi.saveAll(listForSave);
    }

    private LemmaEntity getLemmaEntity(String lemma, int pageId) {
        return lemmaEntities.
                stream().
                filter(lemmaEntity -> lemmaEntity.getLemma().equals(lemma)).
                findFirst().
                orElse(new LemmaEntity());
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
