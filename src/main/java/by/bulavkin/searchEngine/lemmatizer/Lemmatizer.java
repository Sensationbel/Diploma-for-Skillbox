import org.apache.lucene.morphology.LuceneMorphology;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lemmatizer {

    private String text;
    private final LuceneMorphology lm;
    private final Map<String, Integer> numberOfLemm = new HashMap<>();


    public Lemmatizer(String text, LuceneMorphology lm) {
        this.text = text;
        this.lm = lm;
    }

    public void lemmatization(){
        String[] split = text.toLowerCase()
                .replaceAll("[^(а-яё| | -)]*", "")
                .split(" ");
        List<String> listDeleteServiceParts = deleteServiceParts(split);
        List<List<String>> listsNormalForm = listDeleteServiceParts.stream()
                .map(lm::getNormalForms).toList();
        listsNormalForm.forEach(lnf -> {
            int count = numberOfLemm.getOrDefault(lnf.get(0),0);
            numberOfLemm.put(lnf.get(0), count + 1);
        });
        numberOfLemm.forEach((k, v) -> System.out.println(k + " -> " + v));
//        listsNormalForm.stream().flatMap(Collection::stream).forEach(System.out::println);
    }

    private List<String> deleteServiceParts(String[] split) {
        Pattern regex = Pattern.compile("(ПРЕДЛ|СОЮЗ|МЕЖД|ЧАСТ)");
         return Arrays.stream(split).filter(r ->{
            List<String> l = lm.getMorphInfo(r);
            Matcher regexMatcher = regex.matcher(l.get(0));
            return !regexMatcher.find();}).toList();
         }

    public void setText(String text) {
        this.text = text;
    }
}
