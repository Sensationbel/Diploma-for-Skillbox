package by.bulavkin.searchEngine.lemmatizer;

import by.bulavkin.searchEngine.content.ContentFromLemmas;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class Lemmatizer {

    private LuceneMorphology lm;

    {
        try {
            lm = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> lemmatization(String text){
        Map<String, Integer> numberOfLemm = new HashMap<>();
        String[] split = text.toLowerCase()
//                .replaceAll("[^(а-яё|\\s|\\-)]", "")
                .split("(\\-|\\s+)");
        List<String> listDeleteServiceParts = deleteServiceParts(split);
        List<List<String>> listsNormalForm = listDeleteServiceParts.stream()
                .map(lm::getNormalForms).toList();
        listsNormalForm.forEach(lnf -> {
            int count = numberOfLemm.getOrDefault(lnf.get(0),0);
            numberOfLemm.put(lnf.get(0), count + 1);
        });
        return numberOfLemm;
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
