package by.bulavkin.searchEngine.content;

import by.bulavkin.searchEngine.entity.IndexEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class Relevance {

    private float absRelevance;
    private float relRelevance;
    private int pageId;

    private List<HashMap<Integer, Float>> lemmasRank;

    public List<Relevance> addRelevanceList(List<IndexEntity> indexEntities){
        List<Relevance> relevanceList = new ArrayList<>();
        Relevance relevance = null;
        float maxAbsRelevance = 0;
        int tempId = 0;

        for (IndexEntity index : indexEntities) {
            if(tempId != index.getPageId()) {
                tempId = index.getPageId();
                relevance = new Relevance();
                relevance.setPageId(index.getPageId());
                relevance.addLemmasRank(relevance, index);
                relevance.setAbsRelevance(index.getRank());
                relevanceList.add(relevance);
            } else {
                if (relevance != null) {
                    relevance.setAbsRelevance(relevance.getAbsRelevance() + index.getRank());
                    relevance.addLemmasRank(relevance, index);
                }
            }
            if(relevance != null) {
                if (relevance.getAbsRelevance() > maxAbsRelevance) {
                    maxAbsRelevance = relevance.getAbsRelevance();
                }
            }
        }

        addRelRelevanceToList(relevanceList, maxAbsRelevance);
        return getSortRelevanceList(relevanceList);
    }

    private void addLemmasRank(Relevance relevance, IndexEntity index) {
        if(relevance.getLemmasRank() == null){
        relevance.setLemmasRank(new ArrayList<>());
        }
        HashMap<Integer, Float> map = new HashMap<>();
        map.put(index.getLemmaId(), index.getRank());
        relevance.getLemmasRank().add(map);
    }

    private void addRelRelevanceToList(List<Relevance> relevanceList, float maxAbsRelevance){
        relevanceList.forEach(relevance -> relevance.
                setRelRelevance(relevance.
                        getAbsRelevance() / maxAbsRelevance));
    }

    public List<Relevance> getSortRelevanceList(List<Relevance> relevanceList){
        return relevanceList.
                stream().
                sorted(Comparator.
                        comparingDouble(Relevance::getRelRelevance).reversed()).
                toList();
    }
}
