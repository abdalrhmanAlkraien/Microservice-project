package asc.foods.user.service.mapper;

import asc.foods.user.domain.OrderProduct;
import asc.foods.user.service.dto.OrderProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderProduct} and its DTO {@link OrderProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { AscOrderMapper.class, ProductMapper.class })
public interface OrderProductMapper extends EntityMapper<OrderProductDTO, OrderProduct> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    OrderProductDTO toDto(OrderProduct s);
}
