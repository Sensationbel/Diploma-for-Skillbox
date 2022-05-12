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

import java.io.IOException;
import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

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
    private final ArrayList<LemmaEntity> lemmaEntitys;
    private List<FieldEntity> fields;

    public void startAddContentToDatabase() throws IOException, InterruptedException {
        wlp.start();
        psi.saveALL(wlp.getPageEntities());
        fields = fsi.findAll();
        new ArrayList<>(psi.findAll()).
                forEach(pageEntity -> normalizeContent(pageEntity));
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
            Map<String, Integer> lemmas = new HashMap<>(lm.lemmatization(normalizeContent));
            lemmaEntitys.addAll(addLemma(lemmas, page.getId()));
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
}
