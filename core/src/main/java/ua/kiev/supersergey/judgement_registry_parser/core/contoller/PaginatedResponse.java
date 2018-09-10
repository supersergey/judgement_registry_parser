package ua.kiev.supersergey.judgement_registry_parser.core.contoller;

import lombok.Data;

@Data
public class PaginatedResponse<T> {
    private final long collectionSize;
    private final int page;
    private final T payload;
}
