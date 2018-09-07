package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

import java.util.List;

@Repository
public interface KeywordRepository extends PagingAndSortingRepository<Keyword, String> {
    @Query("select Keyword from keyword Keyword where Keyword.status <> 'DELETED' or Keyword.status IS NULL")
    List<Keyword> findByAllNotDeleted(Pageable pageable);
}
