package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Save.class);
        Save save1 = new Save();
        save1.setId(1L);
        Save save2 = new Save();
        save2.setId(save1.getId());
        assertThat(save1).isEqualTo(save2);
        save2.setId(2L);
        assertThat(save1).isNotEqualTo(save2);
        save1.setId(null);
        assertThat(save1).isNotEqualTo(save2);
    }
}
