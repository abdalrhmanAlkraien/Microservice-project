package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeCommentDTO.class);
        LikeCommentDTO likeCommentDTO1 = new LikeCommentDTO();
        likeCommentDTO1.setId(1L);
        LikeCommentDTO likeCommentDTO2 = new LikeCommentDTO();
        assertThat(likeCommentDTO1).isNotEqualTo(likeCommentDTO2);
        likeCommentDTO2.setId(likeCommentDTO1.getId());
        assertThat(likeCommentDTO1).isEqualTo(likeCommentDTO2);
        likeCommentDTO2.setId(2L);
        assertThat(likeCommentDTO1).isNotEqualTo(likeCommentDTO2);
        likeCommentDTO1.setId(null);
        assertThat(likeCommentDTO1).isNotEqualTo(likeCommentDTO2);
    }
}
