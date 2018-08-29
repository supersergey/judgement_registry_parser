package ua.kiev.supersergey.judgement_registry_parser.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Value("${judgement-registry-parser.webClient.basePath}")
    private String basePath;
    @Bean
    public WebClient getWebClient() {
        return WebClient.create(basePath);
    }
}
