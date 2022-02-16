package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderCustomeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderCustomeDTO.class);
        OrderCustomeDTO orderCustomeDTO1 = new OrderCustomeDTO();
        orderCustomeDTO1.setId(1L);
        OrderCustomeDTO orderCustomeDTO2 = new OrderCustomeDTO();
        assertThat(orderCustomeDTO1).isNotEqualTo(orderCustomeDTO2);
        orderCustomeDTO2.setId(orderCustomeDTO1.getId());
        assertThat(orderCustomeDTO1).isEqualTo(orderCustomeDTO2);
        orderCustomeDTO2.setId(2L);
        assertThat(orderCustomeDTO1).isNotEqualTo(orderCustomeDTO2);
        orderCustomeDTO1.setId(null);
        assertThat(orderCustomeDTO1).isNotEqualTo(orderCustomeDTO2);
    }
}
