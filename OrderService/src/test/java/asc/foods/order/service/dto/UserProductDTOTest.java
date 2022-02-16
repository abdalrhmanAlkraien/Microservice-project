package asc.foods.order.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProductDTO.class);
        UserProductDTO userProductDTO1 = new UserProductDTO();
        userProductDTO1.setId(1L);
        UserProductDTO userProductDTO2 = new UserProductDTO();
        assertThat(userProductDTO1).isNotEqualTo(userProductDTO2);
        userProductDTO2.setId(userProductDTO1.getId());
        assertThat(userProductDTO1).isEqualTo(userProductDTO2);
        userProductDTO2.setId(2L);
        assertThat(userProductDTO1).isNotEqualTo(userProductDTO2);
        userProductDTO1.setId(null);
        assertThat(userProductDTO1).isNotEqualTo(userProductDTO2);
    }
}
