package by.bulavkin.searchEngine.dto.statistics;

import lombok.Setter;

import java.sql.Timestamp;

@Setter
public class Detailed{

    private String url;
    private String name;
    private Enum status;
    private Timestamp statusTime;
    private String error;
    private int pages;
    private int lemmas;

    @Override
    public String toString() {
        return "{\"url\": \"" + url +
                "\", \"name\": \"" + name +
                "\", \"status\": \"" + status +
                "\", \"statusTime\": " +
                "\"" + statusTime + "\"" +
                ", \"error\": \"" + error +
                "\", \"pages\": " + pages +
                ", \"lemmas\": " + lemmas +
                "}";

    }
}
