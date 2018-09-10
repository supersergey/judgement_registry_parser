package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.PaginatedResponse;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    PaginatedResponse<Map<String, List<Document>>> loadDashboard(int page, int size, Columns sortColumn, Sort.Direction sortDirection);
}
