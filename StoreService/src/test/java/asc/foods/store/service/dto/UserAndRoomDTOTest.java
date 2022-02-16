package asc.foods.store.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAndRoomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAndRoomDTO.class);
        UserAndRoomDTO userAndRoomDTO1 = new UserAndRoomDTO();
        userAndRoomDTO1.setId(1L);
        UserAndRoomDTO userAndRoomDTO2 = new UserAndRoomDTO();
        assertThat(userAndRoomDTO1).isNotEqualTo(userAndRoomDTO2);
        userAndRoomDTO2.setId(userAndRoomDTO1.getId());
        assertThat(userAndRoomDTO1).isEqualTo(userAndRoomDTO2);
        userAndRoomDTO2.setId(2L);
        assertThat(userAndRoomDTO1).isNotEqualTo(userAndRoomDTO2);
        userAndRoomDTO1.setId(null);
        assertThat(userAndRoomDTO1).isNotEqualTo(userAndRoomDTO2);
    }
}
