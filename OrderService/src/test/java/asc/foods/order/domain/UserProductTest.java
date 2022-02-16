package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProduct.class);
        UserProduct userProduct1 = new UserProduct();
        userProduct1.setId(1L);
        UserProduct userProduct2 = new UserProduct();
        userProduct2.setId(userProduct1.getId());
        assertThat(userProduct1).isEqualTo(userProduct2);
        userProduct2.setId(2L);
        assertThat(userProduct1).isNotEqualTo(userProduct2);
        userProduct1.setId(null);
        assertThat(userProduct1).isNotEqualTo(userProduct2);
    }
}
