package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.PaginatedResponse;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.DocumentRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private KeywordRepository keywordRepository;
    private DocumentRepository documentRepository;

    @Autowired
    public DashboardServiceImpl(KeywordRepository keywordRepository, DocumentRepository documentRepository) {
        this.keywordRepository = keywordRepository;
        this.documentRepository = documentRepository;
    }

    @Transactional
    @Override
    public PaginatedResponse<Map<String, List<Document>>> loadDashboard(int page, int size, Columns sortColumn, Sort.Direction sortDirection) {
        Page<Keyword> keywordsPage = keywordRepository.findByAllNotDeleted(PageRequest.of(page, size, sortDirection, "updatedTs"));
        Map<String, List<Document>> resultMap = keywordsPage
                .getContent().stream()
                .map(Keyword::getKeyword)
                .map(k ->
                        documentRepository.findByKeyword_KeywordIgnoreCase(
                                k, PageRequest.of(0, 5, Sort.Direction.DESC, "createdTs"))
                                .orElseThrow(() -> new RuntimeException("Documents not found, keyword: " + k))
                        .getContent()
                )
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(d -> d.getKeyword().getKeyword(), LinkedHashMap::new, Collectors.toList()));
        resultMap.forEach((key, value) -> value.sort(Comparator.comparing(Document::getCreatedTs).reversed()));
        return new PaginatedResponse<>(keywordsPage.getTotalElements(), page, resultMap);
    }
}
