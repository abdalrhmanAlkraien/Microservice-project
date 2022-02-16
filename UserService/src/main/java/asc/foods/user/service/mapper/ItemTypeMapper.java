package asc.foods.user.service.mapper;

import asc.foods.user.domain.ItemType;
import asc.foods.user.service.dto.ItemTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemType} and its DTO {@link ItemTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface ItemTypeMapper extends EntityMapper<ItemTypeDTO, ItemType> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    ItemTypeDTO toDto(ItemType s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemTypeDTO toDtoId(ItemType itemType);
}
