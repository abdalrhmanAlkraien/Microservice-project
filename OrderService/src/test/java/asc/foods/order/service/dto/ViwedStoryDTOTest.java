package asc.foods.order.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.order.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViwedStoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViwedStoryDTO.class);
        ViwedStoryDTO viwedStoryDTO1 = new ViwedStoryDTO();
        viwedStoryDTO1.setId(1L);
        ViwedStoryDTO viwedStoryDTO2 = new ViwedStoryDTO();
        assertThat(viwedStoryDTO1).isNotEqualTo(viwedStoryDTO2);
        viwedStoryDTO2.setId(viwedStoryDTO1.getId());
        assertThat(viwedStoryDTO1).isEqualTo(viwedStoryDTO2);
        viwedStoryDTO2.setId(2L);
        assertThat(viwedStoryDTO1).isNotEqualTo(viwedStoryDTO2);
        viwedStoryDTO1.setId(null);
        assertThat(viwedStoryDTO1).isNotEqualTo(viwedStoryDTO2);
    }
}
