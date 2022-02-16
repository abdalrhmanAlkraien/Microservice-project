package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaveMapperTest {

    private SaveMapper saveMapper;

    @BeforeEach
    public void setUp() {
        saveMapper = new SaveMapperImpl();
    }
}
