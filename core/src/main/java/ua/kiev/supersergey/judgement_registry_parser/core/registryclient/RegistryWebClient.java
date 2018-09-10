package ua.kiev.supersergey.judgement_registry_parser.core.registryclient;

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
public class RegistryWebClient {
    private final WebClient webClient;

    @Autowired
    public RegistryWebClient(final WebClient webClient)
    {
        this.webClient = webClient;
    }

    public Mono<String> fetchResult(String keyword) {
        System.out.println("Fetching data from registry");
        return fetchResult(keyword, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    public Mono<String> fetchResult(String keyword, LocalDate startDate, LocalDate finishDate) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("SearchExpression", Collections.singletonList(keyword));
        formData.put("PagingInfo.ItemsPerPage", Collections.singletonList("100"));
        formData.put("Liga", Collections.singletonList("true"));
        formData.put("ImportDateBegin", Collections.singletonList(startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        formData.put("ImportDateEnd", Collections.singletonList(finishDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        return webClient.post()
                .syncBody(formData)
                .retrieve()
                .bodyToMono(String.class);
    }
}
