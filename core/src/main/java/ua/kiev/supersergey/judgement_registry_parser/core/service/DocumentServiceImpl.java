package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.DocumentRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
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
                                    .findAllById(incomingDocuments.stream().map(Document::getId).collect(Collectors.toList()))
                                    .spliterator(),
                            false)
                            .map(Document::getId)
                            .collect(Collectors.toList());
            List<Document> documentsToSave = incomingDocuments.stream()
                    .filter(incDoc -> !existingDocumentsIds.contains(incDoc.getId())).collect(Collectors.toList());
            documentRepository.saveAll(documentsToSave);
        });
    }
}
