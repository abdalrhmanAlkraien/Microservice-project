package asc.foods.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AscOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AscOrderDTO.class);
        AscOrderDTO ascOrderDTO1 = new AscOrderDTO();
        ascOrderDTO1.setId(1L);
        AscOrderDTO ascOrderDTO2 = new AscOrderDTO();
        assertThat(ascOrderDTO1).isNotEqualTo(ascOrderDTO2);
        ascOrderDTO2.setId(ascOrderDTO1.getId());
        assertThat(ascOrderDTO1).isEqualTo(ascOrderDTO2);
        ascOrderDTO2.setId(2L);
        assertThat(ascOrderDTO1).isNotEqualTo(ascOrderDTO2);
        ascOrderDTO1.setId(null);
        assertThat(ascOrderDTO1).isNotEqualTo(ascOrderDTO2);
    }
}
