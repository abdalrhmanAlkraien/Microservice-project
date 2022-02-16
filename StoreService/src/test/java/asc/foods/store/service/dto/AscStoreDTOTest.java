package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AscStoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AscStoreDTO.class);
        AscStoreDTO ascStoreDTO1 = new AscStoreDTO();
        ascStoreDTO1.setId(1L);
        AscStoreDTO ascStoreDTO2 = new AscStoreDTO();
        assertThat(ascStoreDTO1).isNotEqualTo(ascStoreDTO2);
        ascStoreDTO2.setId(ascStoreDTO1.getId());
        assertThat(ascStoreDTO1).isEqualTo(ascStoreDTO2);
        ascStoreDTO2.setId(2L);
        assertThat(ascStoreDTO1).isNotEqualTo(ascStoreDTO2);
        ascStoreDTO1.setId(null);
        assertThat(ascStoreDTO1).isNotEqualTo(ascStoreDTO2);
    }
}
