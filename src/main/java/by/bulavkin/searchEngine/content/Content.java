package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.FieldEntity;
import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.FieldServiceIml;
import by.bulavkin.searchEngine.service.IndexServiceImp;
import by.bulavkin.searchEngine.service.LemmaServiceImp;
import by.bulavkin.searchEngine.service.PageServiceImp;
import lombok.RequiredArgsConstructor;
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
public class Content {

    private final PageServiceImp psi;
    private final FieldServiceIml fsi;
    private final LemmaServiceImp lsi;
    private final IndexServiceImp isi;
    private final Lemmatizer lm;
    private final WebLinkParser wlp;

    private final ArrayList<IndexEntity> indexes;
    private final ArrayList<LemmaEntity> lemmaEntities;
    private List<FieldEntity> fields;

    public void startAddContentToDatabase(){
        wlp.start();
        psi.saveALL(wlp.getPageEntities());
        fields = fsi.findAll();
        new ArrayList<>(psi.findAll()).
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
            Map<String, Integer> lemmas = new HashMap<>(lm.getMapWithLemmas(normalizeContent));
            lemmaEntities.addAll(addLemma(lemmas, page.getId()));
            rankCalculation(page, lemmas, field.getWeight());
        });
    }

    private List<LemmaEntity> addLemma(Map<String, Integer> lemmas, int pageId) {
        ArrayList<LemmaEntity> listForSave = new ArrayList<>();
        lemmas.keySet().forEach(lemma -> {
            LemmaEntity lemmaEntity = getLemmaEntity(lemma, pageId);
            if (lemmaEntity.isEmpty()) {
                lemmaEntity.setLemma(lemma);
                lemmaEntity.setFrequency(1);
                lemmaEntity.setPageId(pageId);
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
                filter(lemmaEntity -> lemmaEntity.getPageId() != pageId).
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
