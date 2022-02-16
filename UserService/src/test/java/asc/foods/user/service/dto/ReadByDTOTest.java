package asc.foods.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReadByDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadByDTO.class);
        ReadByDTO readByDTO1 = new ReadByDTO();
        readByDTO1.setId(1L);
        ReadByDTO readByDTO2 = new ReadByDTO();
        assertThat(readByDTO1).isNotEqualTo(readByDTO2);
        readByDTO2.setId(readByDTO1.getId());
        assertThat(readByDTO1).isEqualTo(readByDTO2);
        readByDTO2.setId(2L);
        assertThat(readByDTO1).isNotEqualTo(readByDTO2);
        readByDTO1.setId(null);
        assertThat(readByDTO1).isNotEqualTo(readByDTO2);
    }
}
