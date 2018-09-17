package ua.kiev.supersergey.judgement_registry_parser.core.registryclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
@Slf4j
public class RegistryWebClient {
    private final WebClient webClient;

    @Autowired
    public RegistryWebClient(final WebClient webClient)
    {
        this.webClient = webClient;
    }

    public String fetchResult(String keyword) {
        return fetchResult(keyword, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    public String fetchResult(String keyword, LocalDate startDate, LocalDate finishDate) {
        log.info("Fetching data from registry for keyword: " + keyword + ",startDate: " + startDate + ", finishDate: "+ finishDate);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("SearchExpression", Collections.singletonList(keyword));
        formData.put("PagingInfo.ItemsPerPage", Collections.singletonList("100"));
        formData.put("Liga", Collections.singletonList("true"));
        formData.put("ImportDateBegin", Collections.singletonList(startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        formData.put("ImportDateEnd", Collections.singletonList(finishDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        formData.put("Sort", Collections.singletonList("1"));
        return webClient.post()
                .syncBody(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
