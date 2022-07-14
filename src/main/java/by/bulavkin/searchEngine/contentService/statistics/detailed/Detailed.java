package by.bulavkin.searchEngine.contentService.statistics.detailed;

import lombok.Setter;

@Setter
public class Detailed{

    private String url;
    private String name;
    private Enum status;
    private long statusTime;
    private String error;
    private int pages;
    private int lemmas;

    @Override
    public String toString() {
        return "{\"url\": \"" + url +
                "\", \"name\": \"" + name +
                "\", \"status\": \"" + status +
                "\", \"statusTime\": " + statusTime +
                ", \"error\": \"" + error +
                "\", \"pages\": " + pages +
                ", \"lemmas\": " + lemmas +
                "}";

    }
}
