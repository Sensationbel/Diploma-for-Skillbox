package by.bulavkin.searchEngine.dto.errors;

import by.bulavkin.searchEngine.dto.statistics.StatisticsInterface;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticErrors implements StatisticsInterface {

    private final boolean result;
    private String error;

    public StatisticErrors(String message){
        this.result = false;
        this.error = message;
    }
}
