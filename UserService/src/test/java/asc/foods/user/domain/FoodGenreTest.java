package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodGenreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodGenre.class);
        FoodGenre foodGenre1 = new FoodGenre();
        foodGenre1.setId(1L);
        FoodGenre foodGenre2 = new FoodGenre();
        foodGenre2.setId(foodGenre1.getId());
        assertThat(foodGenre1).isEqualTo(foodGenre2);
        foodGenre2.setId(2L);
        assertThat(foodGenre1).isNotEqualTo(foodGenre2);
        foodGenre1.setId(null);
        assertThat(foodGenre1).isNotEqualTo(foodGenre2);
    }
}
