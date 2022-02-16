package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderStatusHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderStatusHistory.class);
        OrderStatusHistory orderStatusHistory1 = new OrderStatusHistory();
        orderStatusHistory1.setId(1L);
        OrderStatusHistory orderStatusHistory2 = new OrderStatusHistory();
        orderStatusHistory2.setId(orderStatusHistory1.getId());
        assertThat(orderStatusHistory1).isEqualTo(orderStatusHistory2);
        orderStatusHistory2.setId(2L);
        assertThat(orderStatusHistory1).isNotEqualTo(orderStatusHistory2);
        orderStatusHistory1.setId(null);
        assertThat(orderStatusHistory1).isNotEqualTo(orderStatusHistory2);
    }
}
