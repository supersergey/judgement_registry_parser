package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DocumentDto {
    private String id;
    private String decisionType;
    private LocalDate approvalDate;
    private LocalDate legalDate;
    private String judgementForm;
    private String caseNumber;
    private String court;
    private String judge;
    private Date createdTs;
}
