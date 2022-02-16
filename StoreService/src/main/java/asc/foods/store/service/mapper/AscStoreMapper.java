package asc.foods.store.service.mapper;

import asc.foods.store.domain.AscStore;
import asc.foods.store.service.dto.AscStoreDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AscStore} and its DTO {@link AscStoreDTO}.
 */
@Mapper(componentModel = "spring", uses = { FoodGenreMapper.class, StoreTypeMapper.class })
public interface AscStoreMapper extends EntityMapper<AscStoreDTO, AscStore> {
    @Mapping(target = "foodGeners", source = "foodGeners", qualifiedByName = "idSet")
    @Mapping(target = "store", source = "store", qualifiedByName = "id")
    AscStoreDTO toDto(AscStore s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AscStoreDTO toDtoId(AscStore ascStore);

    @Mapping(target = "removeFoodGeners", ignore = true)
    AscStore toEntity(AscStoreDTO ascStoreDTO);
}
