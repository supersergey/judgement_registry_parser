package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.DocumentRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

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
    public Map<String, List<Document>> loadDashboard(int page, int size, Columns sortColumn, Sort.Direction sortDirection) {
        List<Keyword> keywords = keywordRepository.findByAllNotDeleted(PageRequest.of(0, 10, sortDirection, sortColumn.getName()));
        return keywords.stream()
                .map(Keyword::getKeyword)
                .map(k -> documentRepository.findByKeyword_Keyword(k, PageRequest.of(0, 5, Sort.Direction.DESC, "createdTs")))
                .filter(Objects::nonNull)
                .flatMap(slice -> slice.getContent().stream())
                .collect(Collectors.groupingBy(d -> d.getKeyword().getKeyword()));
    }
}
