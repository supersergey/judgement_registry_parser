package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class KeywordDto {
    private String keyword;
    private Date updatedTs;
}
