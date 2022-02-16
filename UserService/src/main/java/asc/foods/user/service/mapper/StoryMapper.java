package asc.foods.user.service.mapper;

import asc.foods.user.domain.Story;
import asc.foods.user.service.dto.StoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Story} and its DTO {@link StoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscStoreMapper.class, AppUserMapper.class })
public interface StoryMapper extends EntityMapper<StoryDTO, Story> {
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    StoryDTO toDto(Story s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoryDTO toDtoId(Story story);
}
