package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AscOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AscOrder.class);
        AscOrder ascOrder1 = new AscOrder();
        ascOrder1.setId(1L);
        AscOrder ascOrder2 = new AscOrder();
        ascOrder2.setId(ascOrder1.getId());
        assertThat(ascOrder1).isEqualTo(ascOrder2);
        ascOrder2.setId(2L);
        assertThat(ascOrder1).isNotEqualTo(ascOrder2);
        ascOrder1.setId(null);
        assertThat(ascOrder1).isNotEqualTo(ascOrder2);
    }
}
