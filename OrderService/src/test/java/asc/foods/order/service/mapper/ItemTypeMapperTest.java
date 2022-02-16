package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTypeMapperTest {

    private ItemTypeMapper itemTypeMapper;

    @BeforeEach
    public void setUp() {
        itemTypeMapper = new ItemTypeMapperImpl();
    }
}
