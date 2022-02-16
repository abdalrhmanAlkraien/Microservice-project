package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReadByTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadBy.class);
        ReadBy readBy1 = new ReadBy();
        readBy1.setId(1L);
        ReadBy readBy2 = new ReadBy();
        readBy2.setId(readBy1.getId());
        assertThat(readBy1).isEqualTo(readBy2);
        readBy2.setId(2L);
        assertThat(readBy1).isNotEqualTo(readBy2);
        readBy1.setId(null);
        assertThat(readBy1).isNotEqualTo(readBy2);
    }
}
