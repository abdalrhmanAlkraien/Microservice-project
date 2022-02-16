package asc.foods.order.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodGenreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodGenreDTO.class);
        FoodGenreDTO foodGenreDTO1 = new FoodGenreDTO();
        foodGenreDTO1.setId(1L);
        FoodGenreDTO foodGenreDTO2 = new FoodGenreDTO();
        assertThat(foodGenreDTO1).isNotEqualTo(foodGenreDTO2);
        foodGenreDTO2.setId(foodGenreDTO1.getId());
        assertThat(foodGenreDTO1).isEqualTo(foodGenreDTO2);
        foodGenreDTO2.setId(2L);
        assertThat(foodGenreDTO1).isNotEqualTo(foodGenreDTO2);
        foodGenreDTO1.setId(null);
        assertThat(foodGenreDTO1).isNotEqualTo(foodGenreDTO2);
    }
}
