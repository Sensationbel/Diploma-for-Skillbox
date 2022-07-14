package by.bulavkin.searchEngine.contentService.statistics;

import lombok.Getter;

@Getter
public class ResultIndexing{

    private boolean result;
    private String error;

    public ResultIndexing() {
        this.result = true;
    }

    public ResultIndexing(String error) {
        this.result = false;
        this.error = error;
    }

    public String getResultForStatistics(){
        if(error != null){
            return String.format("{\"result\": %b, " +
                    "\"error\": \"%s\"}", result, error);
        }
        return String.format("{\"result\": %b, ", result);
    }

    public String getResults(){
        if(error != null){
            return String.format("{\"result\": %b, " +
                    "\"error\": \"%s\"}", result, error);
        }
        return String.format("{\"result\": %b}", result);
    }

}
