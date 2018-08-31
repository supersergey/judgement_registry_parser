package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;

public interface DashboardService {
    List<Keyword> loadDashboard(int page, int size, Columns sortColumn, Sort.Direction sortDirection);
}
