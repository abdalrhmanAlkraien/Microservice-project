package asc.foods.store.service.mapper;

import asc.foods.store.domain.OrderStatusHistory;
import asc.foods.store.service.dto.OrderStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderStatusHistory} and its DTO {@link OrderStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderStatusHistoryMapper extends EntityMapper<OrderStatusHistoryDTO, OrderStatusHistory> {}
