package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, String>{
    Optional<Page<Document>> findByKeyword_KeywordIgnoreCase(String keyword, Pageable pageRequest);
    Optional<Stream<String>> findDocumentIdsByKeyword_KeywordIgnoreCase(String keyword);
    @Query("select doc from document doc where doc.keyword.keyword = :keyword and doc.registryId in :registryIds")
    Page<Document> findAllByKeywordAndRegistryIds(@Param("keyword") String keyword,
                                                  @Param("registryIds") List<String> registryIds,
                                                  Pageable pageRequest);
}
