package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MealCustmizeMapperTest {

    private MealCustmizeMapper mealCustmizeMapper;

    @BeforeEach
    public void setUp() {
        mealCustmizeMapper = new MealCustmizeMapperImpl();
    }
}
