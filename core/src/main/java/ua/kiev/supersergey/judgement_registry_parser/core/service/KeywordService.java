package ua.kiev.supersergey.judgement_registry_parser.core.service;

import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;
import java.util.Optional;

public interface KeywordService {
    List<Keyword> getAllKeywords(int page, int size);
    Optional<Keyword> findOne(String name);
    Keyword addKeyword(Keyword keyword);
    Keyword deleteKeyword(Keyword k);
}
