package asc.foods.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoreFollowerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreFollowerDTO.class);
        StoreFollowerDTO storeFollowerDTO1 = new StoreFollowerDTO();
        storeFollowerDTO1.setId(1L);
        StoreFollowerDTO storeFollowerDTO2 = new StoreFollowerDTO();
        assertThat(storeFollowerDTO1).isNotEqualTo(storeFollowerDTO2);
        storeFollowerDTO2.setId(storeFollowerDTO1.getId());
        assertThat(storeFollowerDTO1).isEqualTo(storeFollowerDTO2);
        storeFollowerDTO2.setId(2L);
        assertThat(storeFollowerDTO1).isNotEqualTo(storeFollowerDTO2);
        storeFollowerDTO1.setId(null);
        assertThat(storeFollowerDTO1).isNotEqualTo(storeFollowerDTO2);
    }
}
