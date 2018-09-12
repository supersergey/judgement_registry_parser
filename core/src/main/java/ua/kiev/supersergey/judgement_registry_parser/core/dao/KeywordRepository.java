package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.Optional;

@Repository
public interface KeywordRepository extends PagingAndSortingRepository<Keyword, String> {
    @Query("select distinct Keyword from keyword Keyword left join document Document on Keyword.keyword = Document.keyword " +
            "where (Document.keyword is not NULL) and (Keyword.status <> 'DELETED' or Keyword.status IS NULL)")
    Page<Keyword> findByAllNotDeleted(Pageable pageable);
    Optional<Keyword> findByKeywordIgnoreCase(String keyword);
}
