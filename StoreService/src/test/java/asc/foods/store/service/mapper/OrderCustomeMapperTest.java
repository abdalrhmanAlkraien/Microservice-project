package asc.foods.store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderCustomeMapperTest {

    private OrderCustomeMapper orderCustomeMapper;

    @BeforeEach
    public void setUp() {
        orderCustomeMapper = new OrderCustomeMapperImpl();
    }
}
