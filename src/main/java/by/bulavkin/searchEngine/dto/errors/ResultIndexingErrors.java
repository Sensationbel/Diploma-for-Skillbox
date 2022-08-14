package by.bulavkin.searchEngine.dto.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultIndexingErrors implements ResultIndexing{

    private boolean result;
    private String error;

    public ResultIndexingErrors(String message){
        this.result = false;
        this.error = message;
    }

}
