package asc.foods.store.service.mapper;

import asc.foods.store.domain.ReadBy;
import asc.foods.store.service.dto.ReadByDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReadBy} and its DTO {@link ReadByDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, MessageMapper.class })
public interface ReadByMapper extends EntityMapper<ReadByDTO, ReadBy> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "message", source = "message", qualifiedByName = "id")
    ReadByDTO toDto(ReadBy s);
}
