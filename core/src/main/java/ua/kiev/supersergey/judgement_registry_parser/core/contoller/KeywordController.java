package ua.kiev.supersergey.judgement_registry_parser.core.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.DocumentDto;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.KeywordDto;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dtoconverter.EntityToDtoConverter;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.service.DocumentService;
import ua.kiev.supersergey.judgement_registry_parser.core.service.KeywordService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping(path = "/api/keyword")
public class KeywordController {
    private final KeywordService keywordService;
    private final DocumentService documentService;
    private final EntityToDtoConverter<Keyword, KeywordDto> keywordConverter;
    private final EntityToDtoConverter<Document, DocumentDto> documentConverter;

    @Autowired
    public KeywordController(KeywordService keywordService,
                             DocumentService documentService,
                             EntityToDtoConverter<Keyword, KeywordDto> keywordConverter,
                             EntityToDtoConverter<Document, DocumentDto> documentConverter) {
        this.keywordService = keywordService;
        this.documentService = documentService;
        this.keywordConverter = keywordConverter;
        this.documentConverter = documentConverter;
    }

    @GetMapping
    public ResponseEntity<?> getAllKeywords(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Keyword> allKeywords = keywordService.getAllKeywords(page, size);
        return CollectionUtils.isEmpty(allKeywords) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nothing is found") : ResponseEntity.ok(keywordConverter.apply(allKeywords));
    }

    @GetMapping(path = "/{keyword}")
    public ResponseEntity<?> getDocumentsByKeyword(
            @PathVariable String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Document> documents = documentService.findDocumentsByKeyword(keyword, page, size);
        return CollectionUtils.isEmpty(documents) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documents not found") : ResponseEntity.ok(documentConverter.apply(documents));
    }

    @PostMapping
    public ResponseEntity<?> addKeyword(@RequestBody @Valid Keyword keyword) {
        if (keywordService.findOne(keyword.getKeyword()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("The specified keyword already exists: " + keyword.getKeyword());
        }
        return ResponseEntity.ok(keywordService.addKeyword(keyword));
    }

    @PutMapping("/{keyword}")
    public ResponseEntity<?> deleteKeyword(@PathVariable("keyword") String keyword) {
        Optional<Keyword> existingKeyword = keywordService.findOne(keyword);
        if (existingKeyword.isPresent()) {
            keywordService.deleteKeyword(existingKeyword.get());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setAccessControlAllowOrigin("*");
            responseHeaders.setAccessControlAllowMethods(Collections.singletonList(HttpMethod.PUT));
            return ResponseEntity.ok(keywordConverter.applySingle(existingKeyword.get()));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete keyword: " + keyword);
    }
}
