package ua.kiev.supersergey.judgement_registry_parser.core.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.service.KeywordService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/keyword")
public class KeywordController {
    private KeywordService keywordService;

    @Autowired
    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping
    public ResponseEntity<List<Keyword>> getAllKeywords(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Keyword> allKeywords = keywordService.getAllKeywords(page, size);
        return CollectionUtils.isEmpty(allKeywords) ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(allKeywords);
    }

    @PostMapping
    public ResponseEntity<String> addKeyword(@RequestBody @Valid Keyword keyword) {
        if (keywordService.findOne(keyword.getKeyword()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        return ResponseEntity.ok(keywordService.addKeyword(keyword));
    }

}
