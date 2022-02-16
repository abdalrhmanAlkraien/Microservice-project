package asc.foods.order.service.mapper;

import asc.foods.order.domain.OrderStatusHistory;
import asc.foods.order.service.dto.OrderStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatusHistory} and its DTO {@link OrderStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderStatusHistoryMapper extends EntityMapper<OrderStatusHistoryDTO, OrderStatusHistory> {}
