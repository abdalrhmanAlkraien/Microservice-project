package asc.foods.store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViwedStoryMapperTest {

    private ViwedStoryMapper viwedStoryMapper;

    @BeforeEach
    public void setUp() {
        viwedStoryMapper = new ViwedStoryMapperImpl();
    }
}
