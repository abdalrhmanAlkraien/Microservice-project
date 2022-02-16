package asc.foods.order.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAndRoomMapperTest {

    private UserAndRoomMapper userAndRoomMapper;

    @BeforeEach
    public void setUp() {
        userAndRoomMapper = new UserAndRoomMapperImpl();
    }
}
