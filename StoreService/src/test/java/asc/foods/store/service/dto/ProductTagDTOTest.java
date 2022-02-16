package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTagDTO.class);
        ProductTagDTO productTagDTO1 = new ProductTagDTO();
        productTagDTO1.setId(1L);
        ProductTagDTO productTagDTO2 = new ProductTagDTO();
        assertThat(productTagDTO1).isNotEqualTo(productTagDTO2);
        productTagDTO2.setId(productTagDTO1.getId());
        assertThat(productTagDTO1).isEqualTo(productTagDTO2);
        productTagDTO2.setId(2L);
        assertThat(productTagDTO1).isNotEqualTo(productTagDTO2);
        productTagDTO1.setId(null);
        assertThat(productTagDTO1).isNotEqualTo(productTagDTO2);
    }
}
