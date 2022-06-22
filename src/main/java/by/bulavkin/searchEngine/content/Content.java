package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.*;
import by.bulavkin.searchEngine.lemmatizer.Lemmatizer;
import by.bulavkin.searchEngine.parsing.DataToParse;
import by.bulavkin.searchEngine.parsing.WebLinkParser;
import by.bulavkin.searchEngine.service.*;
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
    private final SitesServiceImpl ssi;
    private final Lemmatizer lemmatizer;
    private final WebLinkParser linkParser;

    private final DataToParse dataToParse;

    private final ArrayList<IndexEntity> indexes;
    private final ArrayList<LemmaEntity> lemmaEntities;
    private List<FieldEntity> fields;
    private List<Sites> sites;

    public void startIndexingSites(){
        sites.forEach(s -> {

        });
    }

    public void addDataToSitesEntity(){
        List<Sites> list = new ArrayList<>();
        dataToParse.getSites().forEach(s ->{
            Sites site = new Sites();
            site.setName(s.getName());
            site.setUrl(s.getUrl());
            site.setStatus(Status.INDEXING);
            site.setStatusTime(System.currentTimeMillis());
            list.add(site);
        });
        sites = ssi.saveALL(list);
    }

    public void startAddContentToDatabase(){
        linkParser.start();
        psi.saveALL(linkParser.getPageEntities());
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
            Map<String, Integer> lemmas = new HashMap<>(lemmatizer.getMapWithLemmas(normalizeContent));
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
