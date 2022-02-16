package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchDeliveryMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchDeliveryMethod.class);
        BranchDeliveryMethod branchDeliveryMethod1 = new BranchDeliveryMethod();
        branchDeliveryMethod1.setId(1L);
        BranchDeliveryMethod branchDeliveryMethod2 = new BranchDeliveryMethod();
        branchDeliveryMethod2.setId(branchDeliveryMethod1.getId());
        assertThat(branchDeliveryMethod1).isEqualTo(branchDeliveryMethod2);
        branchDeliveryMethod2.setId(2L);
        assertThat(branchDeliveryMethod1).isNotEqualTo(branchDeliveryMethod2);
        branchDeliveryMethod1.setId(null);
        assertThat(branchDeliveryMethod1).isNotEqualTo(branchDeliveryMethod2);
    }
}
