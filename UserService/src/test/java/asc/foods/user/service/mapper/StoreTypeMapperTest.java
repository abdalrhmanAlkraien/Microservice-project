package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoreTypeMapperTest {

    private StoreTypeMapper storeTypeMapper;

    @BeforeEach
    public void setUp() {
        storeTypeMapper = new StoreTypeMapperImpl();
    }
}
