package asc.foods.store.service.mapper;

import asc.foods.store.domain.MealCustmize;
import asc.foods.store.service.dto.MealCustmizeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MealCustmize} and its DTO {@link MealCustmizeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemTypeMapper.class })
public interface MealCustmizeMapper extends EntityMapper<MealCustmizeDTO, MealCustmize> {
    @Mapping(target = "itemType", source = "itemType", qualifiedByName = "id")
    MealCustmizeDTO toDto(MealCustmize s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MealCustmizeDTO toDtoId(MealCustmize mealCustmize);
}
