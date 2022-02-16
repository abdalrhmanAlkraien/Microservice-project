package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoreFollowerMapperTest {

    private StoreFollowerMapper storeFollowerMapper;

    @BeforeEach
    public void setUp() {
        storeFollowerMapper = new StoreFollowerMapperImpl();
    }
}
