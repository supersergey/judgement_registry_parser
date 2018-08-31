package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dtoconverter;

import org.springframework.stereotype.Service;
import ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto.DocumentDto;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;

@Service
public class DocumentEntityToDtoConverter extends EntityToDtoConverter<Document, DocumentDto> {
    @Override
    public DocumentDto applySingle(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setApprovalDate(document.getApprovalDate());
        dto.setCaseNumber(document.getCaseNumber());
        dto.setCourt(document.getCourt());
        dto.setCreatedTs(document.getCreatedTs());
        dto.setDecisionType(document.getDecisionType());
        dto.setId(document.getId());
        dto.setJudge(document.getJudge());
        dto.setJudgementForm(document.getJudgementForm());
        dto.setLegalDate(document.getLegalDate());
        return dto;
    }
}
