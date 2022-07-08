package by.bulavkin.searchEngine.content.statistics.detailed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class Total{

    private int sites;
    private int pages;
    private int lemmas;
    private boolean isIndexing;

    @Override
    public String toString() {
        return "{\"total\": {\"sites\": " + sites +
                ", \"pages\": " + pages +
                ", \"lemmas\": " + lemmas +
                ", \"isIndexing\": " + isIndexing +
                "}, ";
    }
}

