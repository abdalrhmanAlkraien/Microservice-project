package asc.foods.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchDeliveryMethodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchDeliveryMethodDTO.class);
        BranchDeliveryMethodDTO branchDeliveryMethodDTO1 = new BranchDeliveryMethodDTO();
        branchDeliveryMethodDTO1.setId(1L);
        BranchDeliveryMethodDTO branchDeliveryMethodDTO2 = new BranchDeliveryMethodDTO();
        assertThat(branchDeliveryMethodDTO1).isNotEqualTo(branchDeliveryMethodDTO2);
        branchDeliveryMethodDTO2.setId(branchDeliveryMethodDTO1.getId());
        assertThat(branchDeliveryMethodDTO1).isEqualTo(branchDeliveryMethodDTO2);
        branchDeliveryMethodDTO2.setId(2L);
        assertThat(branchDeliveryMethodDTO1).isNotEqualTo(branchDeliveryMethodDTO2);
        branchDeliveryMethodDTO1.setId(null);
        assertThat(branchDeliveryMethodDTO1).isNotEqualTo(branchDeliveryMethodDTO2);
    }
}
