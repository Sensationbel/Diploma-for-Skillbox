package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.FieldEntity;
import by.bulavkin.searchEngine.entity.IndexEntity;
import by.bulavkin.searchEngine.entity.LemmaEntity;
import by.bulavkin.searchEngine.entity.PageEntity;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.repositoties.FieldRepository;
import by.bulavkin.searchEngine.repositoties.PageRepository;
import by.bulavkin.searchEngine.service.IndexServiceImp;
import by.bulavkin.searchEngine.service.LemmaServiceImp;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class Content {

    private final PageRepository pr;
    private final FieldRepository fr;
    private final LemmaServiceImp lsi;
    private final IndexServiceImp isi;
    private final Lemmatizer lm;

    private final HashSet<IndexEntity> indexes;
    private final HashSet<LemmaEntity> lemmaEntitys;

    private int lemmaId;
    private List<FieldEntity> fields;

    public void startAddContentToDatabase() {
        fields = fr.findAll();
        List<PageEntity> pages = new ArrayList<>(pr.findAll());
        pages.forEach(pageEntity -> normalizeContent(pageEntity));
        lsi.saveAll(lemmaEntitys);
        isi.saveIndexesList(indexes);
    }

    public void normalizeContent(PageEntity page) {
        if (page.getCode() != 200) {
            return;
        }
        Document doc = Jsoup.parse(page.getContent());
        fields.forEach(field -> {
            Elements contentQuery = doc.select(field.getSelector());
            String normalizeContent = contentQuery.text().replaceAll("[^ЁёА-я\s]", " ").trim();
            Map<String, Integer> lemmas = new HashMap<>(lm.lemmatization(normalizeContent));
            addLemma(lemmas, page.getId());
            rankCalculation(page, lemmas, field.getWeight());
        });
    }

    private void addLemma(Map<String, Integer> lemmas, int pageId) {
        lemmas.keySet().forEach(lemma -> {
            LemmaEntity lemmaEntity = getLemmaEntity(lemma, pageId);
            if (lemmaEntity.isEmpty()) {
                lemmaEntity.setId(getLemmaId());
                lemmaEntity.setLemma(lemma);
                lemmaEntity.setFrequency(1);
                lemmaEntity.setPageId(pageId);
                lemmaEntitys.add(lemmaEntity);
            } else lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
        });
    }

    private LemmaEntity getLemmaEntity(String lemma, int pageId) {
        return lemmaEntitys.
                stream().
                filter(lemmaEntity -> lemmaEntity.getPageId() != pageId).
                filter(lemmaEntity -> lemmaEntity.getLemma().equals(lemma)).
                findFirst().
                orElse(new LemmaEntity());
    }

    public void rankCalculation(PageEntity page, Map<String, Integer> lemmas, Float weight) {
        int pageId = page.getId();
        lemmas.forEach((k, v) -> {
            int lemmaId = getLemmaEntityId(k);
            IndexEntity indexEntity = getIndexEntity(lemmaId, pageId);
            if (indexEntity.isEmpty()) {
                indexEntity.setPageId(pageId);
                indexEntity.setLemmaId(lemmaId);
                indexEntity.setRank(v * weight);
                indexes.add(indexEntity);
            } else {
                indexEntity.setRank(indexEntity.getRank() + (v * weight));
            }
        });
    }

    private int getLemmaEntityId(String lemma) {
        return lemmaEntitys.
                stream().
                filter(lemmaEntity -> lemmaEntity.getLemma().equals(lemma)).
                mapToInt(lemmaEntity -> lemmaEntity.getId()).
                findFirst().orElseThrow();
    }

    private IndexEntity getIndexEntity(int lemmaId, int pageId) {
        return indexes.
                stream().
                filter(i -> (i.getLemmaId() == lemmaId && i.getPageId() == pageId)).
                findFirst().
                orElse(new IndexEntity());
    }

    private Integer getLemmaId(){
        return this.lemmaId++;
    }
}
