package asc.foods.order.service.mapper;

import asc.foods.order.domain.StoreType;
import asc.foods.order.service.dto.StoreTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoreType} and its DTO {@link StoreTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StoreTypeMapper extends EntityMapper<StoreTypeDTO, StoreType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoreTypeDTO toDtoId(StoreType storeType);
}
