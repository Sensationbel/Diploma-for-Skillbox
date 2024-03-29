package by.bulavkin.searchEngine.dto.statistics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class Total{

    private int sites;
    private int pages;
    private int lemmas;
    private boolean isIndexing;

    @Override
    public String toString() {
        return "{\"sites\": " + sites +
                ", \"pages\": " + pages +
                ", \"lemmas\": " + lemmas +
                ", \"isIndexing\": " + isIndexing +
                "}, ";
    }
}

