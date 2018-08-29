package ua.kiev.supersergey.judgement_registry_parser.core.service;

import reactor.core.publisher.Mono;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.stream.Stream;

public interface DocumentService {
    void saveAll(Keyword keyword, Mono<Stream<Document>> documents);
}
