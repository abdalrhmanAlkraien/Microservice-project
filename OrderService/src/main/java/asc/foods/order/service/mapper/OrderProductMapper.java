package asc.foods.order.service.mapper;

import asc.foods.order.domain.OrderProduct;
import asc.foods.order.service.dto.OrderProductDTO;
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
