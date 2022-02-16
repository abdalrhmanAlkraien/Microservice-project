package asc.foods.user.service.mapper;

import asc.foods.user.domain.MealCustmize;
import asc.foods.user.service.dto.MealCustmizeDTO;
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
