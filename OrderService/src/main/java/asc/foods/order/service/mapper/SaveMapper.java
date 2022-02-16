package asc.foods.order.service.mapper;

import asc.foods.order.domain.Save;
import asc.foods.order.service.dto.SaveDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Save} and its DTO {@link SaveDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, PostMapper.class })
public interface SaveMapper extends EntityMapper<SaveDTO, Save> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    SaveDTO toDto(Save s);
}
