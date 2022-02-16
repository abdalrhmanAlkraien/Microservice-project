package asc.foods.order.service.mapper;

import asc.foods.order.domain.FoodGenre;
import asc.foods.order.service.dto.FoodGenreDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodGenre} and its DTO {@link FoodGenreDTO}.
 */
@Mapper(componentModel = "spring", uses = { StoreTypeMapper.class })
public interface FoodGenreMapper extends EntityMapper<FoodGenreDTO, FoodGenre> {
    @Mapping(target = "storeType", source = "storeType", qualifiedByName = "id")
    FoodGenreDTO toDto(FoodGenre s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodGenreDTO toDtoId(FoodGenre foodGenre);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<FoodGenreDTO> toDtoIdSet(Set<FoodGenre> foodGenre);
}
