package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostMultimediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostMultimediaDTO.class);
        PostMultimediaDTO postMultimediaDTO1 = new PostMultimediaDTO();
        postMultimediaDTO1.setId(1L);
        PostMultimediaDTO postMultimediaDTO2 = new PostMultimediaDTO();
        assertThat(postMultimediaDTO1).isNotEqualTo(postMultimediaDTO2);
        postMultimediaDTO2.setId(postMultimediaDTO1.getId());
        assertThat(postMultimediaDTO1).isEqualTo(postMultimediaDTO2);
        postMultimediaDTO2.setId(2L);
        assertThat(postMultimediaDTO1).isNotEqualTo(postMultimediaDTO2);
        postMultimediaDTO1.setId(null);
        assertThat(postMultimediaDTO1).isNotEqualTo(postMultimediaDTO2);
    }
}
