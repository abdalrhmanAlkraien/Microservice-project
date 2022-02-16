package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AscOrderMapperTest {

    private AscOrderMapper ascOrderMapper;

    @BeforeEach
    public void setUp() {
        ascOrderMapper = new AscOrderMapperImpl();
    }
}
