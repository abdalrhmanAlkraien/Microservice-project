package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAddressMapperTest {

    private UserAddressMapper userAddressMapper;

    @BeforeEach
    public void setUp() {
        userAddressMapper = new UserAddressMapperImpl();
    }
}
