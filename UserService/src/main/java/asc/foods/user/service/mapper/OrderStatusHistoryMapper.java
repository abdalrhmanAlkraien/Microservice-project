package asc.foods.user.service.mapper;

import asc.foods.user.domain.OrderStatusHistory;
import asc.foods.user.service.dto.OrderStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatusHistory} and its DTO {@link OrderStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderStatusHistoryMapper extends EntityMapper<OrderStatusHistoryDTO, OrderStatusHistory> {}
