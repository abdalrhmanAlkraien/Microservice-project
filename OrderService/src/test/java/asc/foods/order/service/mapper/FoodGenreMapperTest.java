package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodGenreMapperTest {

    private FoodGenreMapper foodGenreMapper;

    @BeforeEach
    public void setUp() {
        foodGenreMapper = new FoodGenreMapperImpl();
    }
}
