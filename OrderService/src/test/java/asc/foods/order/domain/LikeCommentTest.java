package asc.foods.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeComment.class);
        LikeComment likeComment1 = new LikeComment();
        likeComment1.setId(1L);
        LikeComment likeComment2 = new LikeComment();
        likeComment2.setId(likeComment1.getId());
        assertThat(likeComment1).isEqualTo(likeComment2);
        likeComment2.setId(2L);
        assertThat(likeComment1).isNotEqualTo(likeComment2);
        likeComment1.setId(null);
        assertThat(likeComment1).isNotEqualTo(likeComment2);
    }
}
