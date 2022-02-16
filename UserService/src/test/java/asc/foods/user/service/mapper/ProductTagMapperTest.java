package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTagMapperTest {

    private ProductTagMapper productTagMapper;

    @BeforeEach
    public void setUp() {
        productTagMapper = new ProductTagMapperImpl();
    }
}
