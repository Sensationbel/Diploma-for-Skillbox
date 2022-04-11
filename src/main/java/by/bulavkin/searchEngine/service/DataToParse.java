package by.bulavkin.searchEngine.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
//@ConfigurationProperties(prefix = "app")
public class DataToParse {

    private String link;  // Пока не решил, нужен он мне или нет!!!
}
