package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dtoconverter;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class EntityToDtoConverter<ENTITY, DTO> implements Function<List<ENTITY>, List<DTO>> {
    public abstract DTO applySingle(ENTITY ENTITY);

    @Override
    public List<DTO> apply(List<ENTITY> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::applySingle).collect(Collectors.toList());
    }
}
