package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

@Repository
public interface KeywordRepository extends PagingAndSortingRepository<Keyword, String> {
}
