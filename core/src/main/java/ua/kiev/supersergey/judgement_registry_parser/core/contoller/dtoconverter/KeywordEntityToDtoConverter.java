package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dtoconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.DocumentDto;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.KeywordDto;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;

@Service
public class KeywordEntityToDtoConverter extends EntityToDtoConverter<Keyword, KeywordDto> {
    private EntityToDtoConverter<Document, DocumentDto> documentConverter;

    @Autowired
    public KeywordEntityToDtoConverter(EntityToDtoConverter<Document, DocumentDto> documentConverter) {
        this.documentConverter = documentConverter;
    }

    @Override
    public KeywordDto applySingle(Keyword keyword) {
        KeywordDto dto = new KeywordDto();
        dto.setKeyword(keyword.getKeyword());
        dto.setUpdatedTs(keyword.getUpdatedTs());
        dto.setDocuments(documentConverter.apply(keyword.getDocuments()));
        return dto;
    }
}
