package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoreTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreType.class);
        StoreType storeType1 = new StoreType();
        storeType1.setId(1L);
        StoreType storeType2 = new StoreType();
        storeType2.setId(storeType1.getId());
        assertThat(storeType1).isEqualTo(storeType2);
        storeType2.setId(2L);
        assertThat(storeType1).isNotEqualTo(storeType2);
        storeType1.setId(null);
        assertThat(storeType1).isNotEqualTo(storeType2);
    }
}
