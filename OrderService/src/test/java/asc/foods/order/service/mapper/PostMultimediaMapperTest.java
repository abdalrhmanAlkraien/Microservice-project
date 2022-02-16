package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostMultimediaMapperTest {

    private PostMultimediaMapper postMultimediaMapper;

    @BeforeEach
    public void setUp() {
        postMultimediaMapper = new PostMultimediaMapperImpl();
    }
}
