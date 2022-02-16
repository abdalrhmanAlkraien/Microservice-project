package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikePostDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikePostDTO.class);
        LikePostDTO likePostDTO1 = new LikePostDTO();
        likePostDTO1.setId(1L);
        LikePostDTO likePostDTO2 = new LikePostDTO();
        assertThat(likePostDTO1).isNotEqualTo(likePostDTO2);
        likePostDTO2.setId(likePostDTO1.getId());
        assertThat(likePostDTO1).isEqualTo(likePostDTO2);
        likePostDTO2.setId(2L);
        assertThat(likePostDTO1).isNotEqualTo(likePostDTO2);
        likePostDTO1.setId(null);
        assertThat(likePostDTO1).isNotEqualTo(likePostDTO2);
    }
}
