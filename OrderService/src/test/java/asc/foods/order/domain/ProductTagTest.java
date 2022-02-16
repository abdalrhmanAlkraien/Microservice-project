package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTag.class);
        ProductTag productTag1 = new ProductTag();
        productTag1.setId(1L);
        ProductTag productTag2 = new ProductTag();
        productTag2.setId(productTag1.getId());
        assertThat(productTag1).isEqualTo(productTag2);
        productTag2.setId(2L);
        assertThat(productTag1).isNotEqualTo(productTag2);
        productTag1.setId(null);
        assertThat(productTag1).isNotEqualTo(productTag2);
    }
}
