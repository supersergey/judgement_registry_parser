package ua.kiev.supersergey.judgement_registry_parser.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.PaginatedResponse;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.KeywordStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;
    private final DocumentService documentService;
    private final static Random RND = new Random();

    @Autowired
    public KeywordServiceImpl(KeywordRepository keywordRepository, DocumentService documentService) {
        this.keywordRepository = keywordRepository;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public PaginatedResponse<List<Keyword>> getAllKeywords(int page, int size) {
        Page<Keyword> response = keywordRepository.findByAllNotDeleted(PageRequest.of(page, size));
        return new PaginatedResponse<>(response.getTotalElements(), page, response.getContent());
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
    public PaginatedResponse<List<Keyword>> findAllByKeyword(String keyword, int page, int size) {
        Page<Keyword> response = keywordRepository.findByKeywordIgnoreCaseContainingAndStatusIsNullOrStatusNot(keyword, KeywordStatus.DELETED, PageRequest.of(page, size));
        return new PaginatedResponse<>(response.getTotalElements(), page, response.getContent());
    }

    @Scheduled(cron = "0 0 5 * * *")
    @Transactional
    public void scheduallyFetchNewDocuments() {
        Optional<Keyword> notUpdatedKeyword;
        do {
            notUpdatedKeyword = keywordRepository.findAllNotUpdatedKeywords(
                    Date.from(LocalDateTime
                            .now()
                            .minusHours(12)
                            .atZone(ZoneOffset.systemDefault())
                            .toInstant()),
                    PageRequest.of(0, 1));
            notUpdatedKeyword.ifPresent(keyword -> Mono.just(keyword)
                    .doOnNext(keywordRepository::save)
                    .map(Keyword::getKeyword)
                    .subscribeOn(Schedulers.elastic())
                    .subscribe(documentService::updateDocumentsForKeyword));
            try {
                Thread.sleep(RND.nextInt(200) * 1000);
            } catch (InterruptedException ex) {
                return;
            }
        } while (notUpdatedKeyword.isPresent());
    }
}
