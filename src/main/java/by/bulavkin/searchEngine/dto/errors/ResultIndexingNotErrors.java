package by.bulavkin.searchEngine.dto.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResultIndexingNotErrors implements ResultIndexing{

    private boolean result;
}
