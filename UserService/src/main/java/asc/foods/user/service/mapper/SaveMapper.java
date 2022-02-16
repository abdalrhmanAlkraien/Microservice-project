package asc.foods.user.service.mapper;

import asc.foods.user.domain.Save;
import asc.foods.user.service.dto.SaveDTO;
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
