package ua.kiev.supersergey.judgement_registry_parser.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.KeywordStatus;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.RegistryWebClient;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser.RegistryResponseParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;
    private final DocumentService documentService;
    private final RegistryWebClient registryWebClient;
    private final RegistryResponseParser parser;

    @Autowired
    public KeywordServiceImpl(KeywordRepository keywordRepository, DocumentService documentService, RegistryWebClient registryWebClient, RegistryResponseParser parser) {
        this.keywordRepository = keywordRepository;
        this.documentService = documentService;
        this.registryWebClient = registryWebClient;
        this.parser = parser;
    }

    @Override
    @Transactional
    public List<Keyword> getAllKeywords(int page, int size) {
        return keywordRepository.findByAllNotDeleted(PageRequest.of(page, size)).getContent();
    }

    @Override
    @Transactional
    public Optional<Keyword> findOne(String name) {
        return keywordRepository.findById(name);
    }

    @Override
    @Transactional
    public Keyword addKeyword(Keyword keyword) {
        Keyword savedKeyword = keywordRepository.save(keyword);
        Mono.just(savedKeyword)
                .map(Keyword::getKeyword)
                .subscribeOn(Schedulers.parallel())
                .flatMap(registryWebClient::fetchResult)
                .map(parser::parse)
                .doOnNext(documentStream -> {
                    List<Document> documents = documentStream.collect(Collectors.toList());
                    documents.forEach(doc -> doc.setKeyword(keyword));
                    keyword.setDocuments(documents);
                    keywordRepository.save(keyword);
                    log.info("Saved keyword" + keyword.getKeyword() + ", doc.size= " + keyword.getDocuments().size());
                })
                .subscribe();
        return savedKeyword;
    }

    @Override
    @Transactional
    public void deleteKeyword(Keyword keyword) {
        log.info("Deleting keyword: " + keyword.getKeyword());
        keywordRepository.delete(keyword);
    }
}
