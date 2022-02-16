package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProductMapperTest {

    private UserProductMapper userProductMapper;

    @BeforeEach
    public void setUp() {
        userProductMapper = new UserProductMapperImpl();
    }
}
