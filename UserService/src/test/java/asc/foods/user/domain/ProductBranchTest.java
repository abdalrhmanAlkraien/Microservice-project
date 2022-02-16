package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductBranchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBranch.class);
        ProductBranch productBranch1 = new ProductBranch();
        productBranch1.setId(1L);
        ProductBranch productBranch2 = new ProductBranch();
        productBranch2.setId(productBranch1.getId());
        assertThat(productBranch1).isEqualTo(productBranch2);
        productBranch2.setId(2L);
        assertThat(productBranch1).isNotEqualTo(productBranch2);
        productBranch1.setId(null);
        assertThat(productBranch1).isNotEqualTo(productBranch2);
    }
}
