package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoreFollowerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreFollower.class);
        StoreFollower storeFollower1 = new StoreFollower();
        storeFollower1.setId(1L);
        StoreFollower storeFollower2 = new StoreFollower();
        storeFollower2.setId(storeFollower1.getId());
        assertThat(storeFollower1).isEqualTo(storeFollower2);
        storeFollower2.setId(2L);
        assertThat(storeFollower1).isNotEqualTo(storeFollower2);
        storeFollower1.setId(null);
        assertThat(storeFollower1).isNotEqualTo(storeFollower2);
    }
}
