package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;

public interface DocumentRepository extends PagingAndSortingRepository<Document, String>{
}
