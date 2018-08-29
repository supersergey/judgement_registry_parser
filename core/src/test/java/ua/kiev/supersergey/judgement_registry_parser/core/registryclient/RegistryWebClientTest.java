package ua.kiev.supersergey.judgement_registry_parser.core.registryclient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser.RegistryResponseParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class RegistryWebClientTest {

    private RegistryWebClient registryWebClient;
    private WebClient webClient;
    private final static String KEYWORD = "Вітал плюс";

    @Before
    public void init() {
        webClient = WebClient.create("http://reyestr.court.gov.ua");
        registryWebClient = new RegistryWebClient(webClient);
    }

    @Test
    public void fetchResult() throws Exception{
        Mono<String> result = registryWebClient.fetchResult(KEYWORD,
                LocalDate.now().minusMonths(1),
                LocalDate.now());
        result.subscribe(this::writeStringToFile);
        Thread.sleep(5000);
    }

    private void writeStringToFile(String s) {
            try {
                Files.write(Paths.get("output.html"), s.getBytes());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }
}