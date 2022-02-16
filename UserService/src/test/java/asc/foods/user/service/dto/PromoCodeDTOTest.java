package asc.foods.user.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromoCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoCodeDTO.class);
        PromoCodeDTO promoCodeDTO1 = new PromoCodeDTO();
        promoCodeDTO1.setId(1L);
        PromoCodeDTO promoCodeDTO2 = new PromoCodeDTO();
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
        promoCodeDTO2.setId(promoCodeDTO1.getId());
        assertThat(promoCodeDTO1).isEqualTo(promoCodeDTO2);
        promoCodeDTO2.setId(2L);
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
        promoCodeDTO1.setId(null);
        assertThat(promoCodeDTO1).isNotEqualTo(promoCodeDTO2);
    }
}
