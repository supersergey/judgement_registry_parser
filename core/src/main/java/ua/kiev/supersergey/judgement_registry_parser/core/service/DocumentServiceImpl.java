package ua.kiev.supersergey.judgement_registry_parser.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.DocumentRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.RegistryWebClient;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser.RegistryResponseParser;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;
    private final KeywordRepository keywordRepository;
    private final RegistryWebClient registryWebClient;
    private final RegistryResponseParser parser;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, KeywordRepository keywordRepository, RegistryWebClient registryWebClient, RegistryResponseParser parser) {
        this.documentRepository = documentRepository;
        this.keywordRepository = keywordRepository;
        this.registryWebClient = registryWebClient;
        this.parser = parser;
    }

    @Override
    @Transactional
    public void saveAll(Keyword keyword, Mono<Stream<Document>> documents) {
        documents.subscribe(documentStream -> {
            List<Document> incomingDocuments = documentStream.collect(Collectors.toList());
            incomingDocuments.forEach(incDoc -> incDoc.setKeyword(keyword));
            List<String> existingDocumentsIds =
                    StreamSupport.stream(
                            documentRepository
                                    .findAllById(incomingDocuments.stream().map(Document::getRegistryId).collect(Collectors.toList()))
                                    .spliterator(),
                            false)
                            .map(Document::getRegistryId)
                            .collect(Collectors.toList());
            List<Document> documentsToSave = incomingDocuments.stream()
                    .filter(incDoc -> !existingDocumentsIds.contains(incDoc.getRegistryId())).collect(Collectors.toList());
            documentRepository.saveAll(documentsToSave);
        });
    }

    @Override
    public List<Document> findDocumentsByKeyword(String keyword, Integer page, Integer size) {
        return documentRepository.findByKeyword_KeywordIgnoreCase(keyword, PageRequest.of(page, size, Sort.Direction.DESC, Columns.CREATED_TS.getName()))
                .orElseThrow(() -> new RuntimeException("Documents not found")).getContent();
    }

    @Override
    @Transactional
    public void updateDocumentsForKeyword(String keyword) {
        log.info("Entering updateDocumentsForKeyword");
        final List<String> existingDocumentIds = documentRepository.findDocumentIdsByKeyword_KeywordIgnoreCase(keyword)
                .orElseThrow(() -> new RuntimeException("Keyword not found")).collect(Collectors.toList());
        List<Document> newDocuments = Stream.of(keyword)
                .map(registryWebClient::fetchResult)
                .flatMap(parser::parse)
                .filter(newDoc -> !existingDocumentIds.contains(newDoc.getRegistryId()))
                .collect(Collectors.toList());
        final Keyword newKeyword = keywordRepository.findByKeywordIgnoreCase(keyword).orElseThrow(() -> new RuntimeException("Keyword not found: " + keyword));
        newDocuments.forEach(doc -> doc.setKeyword(newKeyword));
        newKeyword.getDocuments().addAll(newDocuments);
        newKeyword.setUpdatedTs(new Date());
        documentRepository.saveAll(newDocuments);
        log.info("Saved keyword" + newKeyword.getKeyword() + ", doc.size= " + newKeyword.getDocuments().size());
    }
}
