package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikePostMapperTest {

    private LikePostMapper likePostMapper;

    @BeforeEach
    public void setUp() {
        likePostMapper = new LikePostMapperImpl();
    }
}
