package ua.kiev.supersergey.judgement_registry_parser.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.PaginatedResponse;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.KeywordStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;
    private final DocumentService documentService;

    @Autowired
    public KeywordServiceImpl(KeywordRepository keywordRepository, DocumentService documentService) {
        this.keywordRepository = keywordRepository;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public PaginatedResponse<List<Keyword>> getAllKeywords(int page, int size) {
        Page<Keyword> response = keywordRepository.findByAllNotDeleted(PageRequest.of(page, size));
        return new PaginatedResponse<List<Keyword>>(response.getTotalElements(), page, response.getContent());
    }

    @Override
    @Transactional
    public Optional<Keyword> findOne(String name) {
        return keywordRepository.findByKeywordIgnoreCase(name);
    }

    @Override
    @Transactional
    public Keyword addKeyword(Keyword keyword) {
        keyword.setStatus(null);
        Keyword savedKeyword = keywordRepository.save(keyword);
        Mono.just(keyword.getKeyword())
                .subscribeOn(Schedulers.elastic())
                .subscribe(documentService::updateDocumentsForKeyword);
        log.info("Saved keyword: " + keyword.getKeyword() + ", documents size: " + keyword.getDocuments().size());
        return savedKeyword;
    }

    @Override
    @Transactional
    public Keyword updateKeyword(Keyword keyword) {
        log.info("Entering updateKeyword");
        Mono.just(keyword.getKeyword())
                .subscribeOn(Schedulers.elastic())
                .subscribe(documentService::updateDocumentsForKeyword);
        keyword.setUpdatedTs(new Date());
        log.info("Exiting updateKeyword");
        return keywordRepository.save(keyword);
    }

    @Override
    @Transactional
    public Keyword deleteKeyword(Keyword keyword) {
        log.info("Deleting keyword: " + keyword.getKeyword());
        keyword.setStatus(KeywordStatus.DELETED);
        return keywordRepository.save(keyword);
    }

    @Override
    public Optional<List<Keyword>> findAllByKeyword(String keyword, int page, int size) {
        return keywordRepository.findByKeywordIgnoreCaseContainingAndStatusIsNullOrStatusNot(keyword, KeywordStatus.DELETED, PageRequest.of(page, size));
    }
}
