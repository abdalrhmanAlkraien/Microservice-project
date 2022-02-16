package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderStatusHistoryMapperTest {

    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @BeforeEach
    public void setUp() {
        orderStatusHistoryMapper = new OrderStatusHistoryMapperImpl();
    }
}
