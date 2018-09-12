package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeywordDto {
    private String keyword;
    private LocalDateTime updatedTs;
}
