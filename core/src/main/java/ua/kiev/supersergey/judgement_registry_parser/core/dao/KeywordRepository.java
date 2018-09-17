package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.KeywordStatus;

import java.util.Date;
import java.util.Optional;

@Repository
public interface KeywordRepository extends PagingAndSortingRepository<Keyword, String> {
    @Query("select distinct Keyword from keyword Keyword left join document Document on Keyword.keyword = Document.keyword " +
            "where (Document.keyword is not NULL) and (Keyword.status <> 'DELETED' or Keyword.status IS NULL)")
    Page<Keyword> findByAllNotDeleted(Pageable pageable);
//    @Query("select Keyword from keyword Keyword join fetch Keyword.documents where lower(Keyword.keyword) = lower(:keyword)")
//    Optional<Keyword> findByKeywordIgnoreCase(@Param("keyword") String keyword);
    Optional<Keyword> findByKeywordIgnoreCase(String keyword);
    Page<Keyword> findByKeywordIgnoreCaseContainingAndStatusIsNullOrStatusNot(String keyword, KeywordStatus status, Pageable page);
    @Query("select distinct Keyword from keyword Keyword where Keyword.createdTs < :date and Keyword.status is NULL")
    Optional<Keyword> findAllNotUpdatedKeywords(@Param("date")Date date, Pageable pageable);
}
