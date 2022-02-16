package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaveDTO.class);
        SaveDTO saveDTO1 = new SaveDTO();
        saveDTO1.setId(1L);
        SaveDTO saveDTO2 = new SaveDTO();
        assertThat(saveDTO1).isNotEqualTo(saveDTO2);
        saveDTO2.setId(saveDTO1.getId());
        assertThat(saveDTO1).isEqualTo(saveDTO2);
        saveDTO2.setId(2L);
        assertThat(saveDTO1).isNotEqualTo(saveDTO2);
        saveDTO1.setId(null);
        assertThat(saveDTO1).isNotEqualTo(saveDTO2);
    }
}
