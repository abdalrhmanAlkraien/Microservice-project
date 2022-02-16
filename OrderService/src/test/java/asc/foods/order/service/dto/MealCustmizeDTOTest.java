package asc.foods.order.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealCustmizeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealCustmizeDTO.class);
        MealCustmizeDTO mealCustmizeDTO1 = new MealCustmizeDTO();
        mealCustmizeDTO1.setId(1L);
        MealCustmizeDTO mealCustmizeDTO2 = new MealCustmizeDTO();
        assertThat(mealCustmizeDTO1).isNotEqualTo(mealCustmizeDTO2);
        mealCustmizeDTO2.setId(mealCustmizeDTO1.getId());
        assertThat(mealCustmizeDTO1).isEqualTo(mealCustmizeDTO2);
        mealCustmizeDTO2.setId(2L);
        assertThat(mealCustmizeDTO1).isNotEqualTo(mealCustmizeDTO2);
        mealCustmizeDTO1.setId(null);
        assertThat(mealCustmizeDTO1).isNotEqualTo(mealCustmizeDTO2);
    }
}
