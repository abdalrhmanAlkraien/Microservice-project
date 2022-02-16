package asc.foods.order.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductBranchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBranchDTO.class);
        ProductBranchDTO productBranchDTO1 = new ProductBranchDTO();
        productBranchDTO1.setId(1L);
        ProductBranchDTO productBranchDTO2 = new ProductBranchDTO();
        assertThat(productBranchDTO1).isNotEqualTo(productBranchDTO2);
        productBranchDTO2.setId(productBranchDTO1.getId());
        assertThat(productBranchDTO1).isEqualTo(productBranchDTO2);
        productBranchDTO2.setId(2L);
        assertThat(productBranchDTO1).isNotEqualTo(productBranchDTO2);
        productBranchDTO1.setId(null);
        assertThat(productBranchDTO1).isNotEqualTo(productBranchDTO2);
    }
}
