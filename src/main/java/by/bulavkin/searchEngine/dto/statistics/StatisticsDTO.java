package by.bulavkin.searchEngine.dto.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsDTO implements StatisticsInterface{

    private boolean result;
    private Statistics statistics;

    public StatisticsDTO(boolean result, Statistics statistics) {
        this.result = result;
        this.statistics = statistics;
    }
}
