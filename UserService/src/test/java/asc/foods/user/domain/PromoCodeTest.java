package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromoCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoCode.class);
        PromoCode promoCode1 = new PromoCode();
        promoCode1.setId(1L);
        PromoCode promoCode2 = new PromoCode();
        promoCode2.setId(promoCode1.getId());
        assertThat(promoCode1).isEqualTo(promoCode2);
        promoCode2.setId(2L);
        assertThat(promoCode1).isNotEqualTo(promoCode2);
        promoCode1.setId(null);
        assertThat(promoCode1).isNotEqualTo(promoCode2);
    }
}
