package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViwedStoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViwedStory.class);
        ViwedStory viwedStory1 = new ViwedStory();
        viwedStory1.setId(1L);
        ViwedStory viwedStory2 = new ViwedStory();
        viwedStory2.setId(viwedStory1.getId());
        assertThat(viwedStory1).isEqualTo(viwedStory2);
        viwedStory2.setId(2L);
        assertThat(viwedStory1).isNotEqualTo(viwedStory2);
        viwedStory1.setId(null);
        assertThat(viwedStory1).isNotEqualTo(viwedStory2);
    }
}
