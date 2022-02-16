package asc.foods.store.service.mapper;

import asc.foods.store.domain.ViwedStory;
import asc.foods.store.service.dto.ViwedStoryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ViwedStory} and its DTO {@link ViwedStoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ViwedStoryMapper extends EntityMapper<ViwedStoryDTO, ViwedStory> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ViwedStoryDTO> toDtoIdSet(Set<ViwedStory> viwedStory);
}
