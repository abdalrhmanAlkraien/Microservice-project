package asc.foods.store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductBranchMapperTest {

    private ProductBranchMapper productBranchMapper;

    @BeforeEach
    public void setUp() {
        productBranchMapper = new ProductBranchMapperImpl();
    }
}
