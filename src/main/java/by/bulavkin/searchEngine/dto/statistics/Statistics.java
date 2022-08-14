package by.bulavkin.searchEngine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Statistics {

    private Total total;
    private List<Detailed> detailed;
}
