package by.bulavkin.searchEngine.lemmatizer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@Component
public class Lemmatizer {

    private LuceneMorphology lm;

    {
        try {
            lm = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getMapWithLemmas(String text){
        Map<String, Integer> numberOfLemm = new HashMap<>();
        getListWithNormalFormsFromText(text).forEach(lnf -> {
            int count = numberOfLemm.getOrDefault(lnf,0);
            numberOfLemm.put(lnf, count + 1);
        });
        return numberOfLemm;
    }

    public List<String> getListWithNormalFormsFromText(String text){
        String[] split = text.toLowerCase()
                .split("(-|\\s+)");
        List<String> listDeleteServiceParts = deleteServiceParts(split);
        List<String> listNormalForms = new ArrayList<>();
        listDeleteServiceParts.
                stream().
                map(lm::getNormalForms).
                forEach(lemma -> listNormalForms.add(lemma.get(0)));
        return listNormalForms;

    }

    private List<String> deleteServiceParts(String[] split) {
        Pattern regex = Pattern.compile("(ПРЕДЛ|СОЮЗ|МЕЖД|ЧАСТ)");
         return Arrays.stream(split)
                 .map(sp -> sp.replaceAll("\\.", ""))
                 .filter(sp -> sp.matches("[а-яё]+"))
                 .filter(sp ->{
            List<String> l = lm.getMorphInfo(sp);
            Matcher regexMatcher = regex.matcher(l.get(0));
            return !regexMatcher.find();}).toList();
         }
}
