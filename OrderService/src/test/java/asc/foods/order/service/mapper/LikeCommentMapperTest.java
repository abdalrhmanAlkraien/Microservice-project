package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeCommentMapperTest {

    private LikeCommentMapper likeCommentMapper;

    @BeforeEach
    public void setUp() {
        likeCommentMapper = new LikeCommentMapperImpl();
    }
}
