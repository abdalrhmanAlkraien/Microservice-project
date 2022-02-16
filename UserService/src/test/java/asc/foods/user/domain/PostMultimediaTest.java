package asc.foods.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.user.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostMultimediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostMultimedia.class);
        PostMultimedia postMultimedia1 = new PostMultimedia();
        postMultimedia1.setId(1L);
        PostMultimedia postMultimedia2 = new PostMultimedia();
        postMultimedia2.setId(postMultimedia1.getId());
        assertThat(postMultimedia1).isEqualTo(postMultimedia2);
        postMultimedia2.setId(2L);
        assertThat(postMultimedia1).isNotEqualTo(postMultimedia2);
        postMultimedia1.setId(null);
        assertThat(postMultimedia1).isNotEqualTo(postMultimedia2);
    }
}
