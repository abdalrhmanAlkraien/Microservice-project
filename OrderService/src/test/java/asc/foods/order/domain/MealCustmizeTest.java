package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealCustmizeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealCustmize.class);
        MealCustmize mealCustmize1 = new MealCustmize();
        mealCustmize1.setId(1L);
        MealCustmize mealCustmize2 = new MealCustmize();
        mealCustmize2.setId(mealCustmize1.getId());
        assertThat(mealCustmize1).isEqualTo(mealCustmize2);
        mealCustmize2.setId(2L);
        assertThat(mealCustmize1).isNotEqualTo(mealCustmize2);
        mealCustmize1.setId(null);
        assertThat(mealCustmize1).isNotEqualTo(mealCustmize2);
    }
}
