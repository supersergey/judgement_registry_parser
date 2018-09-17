package ua.kiev.supersergey.judgement_registry_parser.core.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import ua.kiev.supersergey.judgement_registry_parser.core.entity.KeywordStatus;
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
        PaginatedResponse<List<Keyword>> allKeywords = keywordService.getAllKeywords(page, size);
        return CollectionUtils.isEmpty(allKeywords.getPayload()) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Данных нет")
                : ResponseEntity.ok(new PaginatedResponse<>(allKeywords.getCollectionSize(), allKeywords.getPage(), keywordConverter.apply(allKeywords.getPayload())));
    }

    @GetMapping(path = "/details/{keyword}")
    public ResponseEntity<?> getDocumentsByKeyword(
            @PathVariable String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Document> documents = documentService.findDocumentsByKeyword(keyword, page, size);
        return CollectionUtils.isEmpty(documents) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Документы по ключевому слову [" + keyword + "] не найдены.") : ResponseEntity.ok(documentConverter.apply(documents));
    }

    @PostMapping
    public ResponseEntity<?> addKeyword(@RequestBody @Valid Keyword keyword) {
        Optional<Keyword> existingKeyword = keywordService.findOne(keyword.getKeyword());
        if (existingKeyword.isPresent() && existingKeyword.get().getStatus() != KeywordStatus.DELETED) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ключевое слово уже зарегистрировано в системе: " + keyword.getKeyword());
        }
        if (existingKeyword.isPresent() && existingKeyword.get().getStatus() == KeywordStatus.DELETED) {
            keyword = existingKeyword.get();
        }
        return ResponseEntity.ok(keywordConverter.applySingle(keywordService.addKeyword(keyword)));
    }

    @PutMapping
    public ResponseEntity<?> updateKeyword(@RequestBody @Valid Keyword keyword) {
        Optional<Keyword> existingKeyword = keywordService.findOne(keyword.getKeyword());
        if (existingKeyword.isPresent() && existingKeyword.get().getStatus() != KeywordStatus.DELETED) {
            Keyword updatedKeyword = keywordService.updateKeyword(existingKeyword.get());
            return ResponseEntity.ok(keywordConverter.applySingle(updatedKeyword));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ключевое слово не найдено: " + keyword);

        }
    }

    @DeleteMapping("/{keyword}")
    public ResponseEntity<?> deleteKeyword(@PathVariable("keyword") String keyword) {
        Optional<Keyword> existingKeyword = keywordService.findOne(keyword);
        if (existingKeyword.isPresent()) {
            keywordService.deleteKeyword(existingKeyword.get());
            return ResponseEntity.ok(keywordConverter.applySingle(existingKeyword.get()));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка при удаленини ключевого слова: " + keyword);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<?> findByKeyword(@PathVariable("keyword") String keyword,
                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        Optional<List<Keyword>> existingKeyword = keywordService.findAllByKeyword(keyword, page, size);
        if (existingKeyword.isPresent()) {
            return ResponseEntity.ok(keywordConverter.apply(existingKeyword.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ключевое слово не найдено: " + keyword);
        }
    }
}
