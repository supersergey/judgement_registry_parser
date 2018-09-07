package ua.kiev.supersergey.judgement_registry_parser.core.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.Columns;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.DocumentDto;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.KeywordDto;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dtoconverter.EntityToDtoConverter;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.service.DashboardService;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/dashboard")
public class DashboardController {
    private DashboardService dashboardService;
    private EntityToDtoConverter<Keyword, KeywordDto> converter;
    private EntityToDtoConverter<Document, DocumentDto> documentConverter;

    @Autowired
    public DashboardController(DashboardService dashboardService, EntityToDtoConverter<Keyword, KeywordDto> converter, EntityToDtoConverter<Document, DocumentDto> documentConverter) {
        this.dashboardService = dashboardService;
        this.converter = converter;
        this.documentConverter = documentConverter;
    }

    @GetMapping
    public ResponseEntity<?> getDashboardData(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "KEYWORD") Columns sortColumn) {
        Map<String, List<Document>> dashboardMap = dashboardService.loadDashboard(page, size, sortColumn, deriveSortDirection(sortDirection));
        if (CollectionUtils.isEmpty(dashboardMap)) {
            new ResponseEntity<>("Dashboard data not found", HttpStatus.NOT_FOUND);
        }
        Map<String, List<DocumentDto>> result = new HashMap<>();
        dashboardMap.forEach((keyword, documents) -> {
            result.put(keyword, documentConverter.apply(documents));
        });
        return ResponseEntity.ok().body(result);
    }

    private Sort.Direction deriveSortDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("ASC")) {
            return Sort.Direction.ASC;
        }
        if (sortDirection.equalsIgnoreCase("DESC")) {
            return Sort.Direction.DESC;
        }
        throw new IllegalArgumentException("Unsupported sort direction: " + sortDirection);
    }
}
