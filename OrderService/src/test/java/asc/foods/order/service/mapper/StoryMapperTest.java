package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoryMapperTest {

    private StoryMapper storyMapper;

    @BeforeEach
    public void setUp() {
        storyMapper = new StoryMapperImpl();
    }
}
