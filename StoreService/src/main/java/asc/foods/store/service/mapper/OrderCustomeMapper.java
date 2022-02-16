package asc.foods.store.service.mapper;

import asc.foods.store.domain.OrderCustome;
import asc.foods.store.service.dto.OrderCustomeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderCustome} and its DTO {@link OrderCustomeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, AscOrderMapper.class, MealCustmizeMapper.class })
public interface OrderCustomeMapper extends EntityMapper<OrderCustomeDTO, OrderCustome> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "ascOrder", source = "ascOrder", qualifiedByName = "id")
    @Mapping(target = "mealCustmize", source = "mealCustmize", qualifiedByName = "id")
    OrderCustomeDTO toDto(OrderCustome s);
}
