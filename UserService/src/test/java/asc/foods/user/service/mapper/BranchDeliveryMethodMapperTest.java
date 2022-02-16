package asc.foods.user.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchDeliveryMethodMapperTest {

    private BranchDeliveryMethodMapper branchDeliveryMethodMapper;

    @BeforeEach
    public void setUp() {
        branchDeliveryMethodMapper = new BranchDeliveryMethodMapperImpl();
    }
}
