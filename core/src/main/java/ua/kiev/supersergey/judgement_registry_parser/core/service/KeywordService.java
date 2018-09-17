package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.transaction.annotation.Transactional;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface KeywordService {
    List<Keyword> getAllKeywords(int page, int size);
    Optional<Keyword> findOne(String name);
    Keyword addKeyword(Keyword keyword);
    Keyword updateKeyword(Keyword keyword);
    Keyword deleteKeyword(Keyword k);
    Optional<List<Keyword>> findAllByKeyword(String keyword, int page, int size);
}
