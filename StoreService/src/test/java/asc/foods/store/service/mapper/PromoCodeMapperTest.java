package asc.foods.store.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromoCodeMapperTest {

    private PromoCodeMapper promoCodeMapper;

    @BeforeEach
    public void setUp() {
        promoCodeMapper = new PromoCodeMapperImpl();
    }
}
