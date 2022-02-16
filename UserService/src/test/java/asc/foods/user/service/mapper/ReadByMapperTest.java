package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadByMapperTest {

    private ReadByMapper readByMapper;

    @BeforeEach
    public void setUp() {
        readByMapper = new ReadByMapperImpl();
    }
}
