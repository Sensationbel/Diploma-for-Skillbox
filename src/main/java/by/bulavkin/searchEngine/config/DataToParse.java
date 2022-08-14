package by.bulavkin.searchEngine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class DataToParse {

    private String userAgent;
    private String referrer;

    private List<DataSites> sites;

    @Getter
    @Setter
    public static class DataSites{

        private String url;
        private String name;
    }


}
