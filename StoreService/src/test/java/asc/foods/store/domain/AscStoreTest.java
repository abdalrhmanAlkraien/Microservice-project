package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AscStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AscStore.class);
        AscStore ascStore1 = new AscStore();
        ascStore1.setId(1L);
        AscStore ascStore2 = new AscStore();
        ascStore2.setId(ascStore1.getId());
        assertThat(ascStore1).isEqualTo(ascStore2);
        ascStore2.setId(2L);
        assertThat(ascStore1).isNotEqualTo(ascStore2);
        ascStore1.setId(null);
        assertThat(ascStore1).isNotEqualTo(ascStore2);
    }
}
