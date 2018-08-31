package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private KeywordRepository keywordRepository;

    @Autowired
    public DashboardServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Transactional
    @Override
    public List<Keyword> loadDashboard(int page, int size, Columns sortColumn, Sort.Direction sortDirection) {
        return keywordRepository.findAll(PageRequest.of(0, 10, sortDirection, sortColumn.getName())).getContent();
    }
}
