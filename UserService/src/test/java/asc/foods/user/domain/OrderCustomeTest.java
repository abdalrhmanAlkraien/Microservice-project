package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderCustomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderCustome.class);
        OrderCustome orderCustome1 = new OrderCustome();
        orderCustome1.setId(1L);
        OrderCustome orderCustome2 = new OrderCustome();
        orderCustome2.setId(orderCustome1.getId());
        assertThat(orderCustome1).isEqualTo(orderCustome2);
        orderCustome2.setId(2L);
        assertThat(orderCustome1).isNotEqualTo(orderCustome2);
        orderCustome1.setId(null);
        assertThat(orderCustome1).isNotEqualTo(orderCustome2);
    }
}
