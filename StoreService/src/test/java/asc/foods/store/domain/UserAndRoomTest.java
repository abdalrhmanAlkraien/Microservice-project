package asc.foods.store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import asc.foods.store.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAndRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAndRoom.class);
        UserAndRoom userAndRoom1 = new UserAndRoom();
        userAndRoom1.setId(1L);
        UserAndRoom userAndRoom2 = new UserAndRoom();
        userAndRoom2.setId(userAndRoom1.getId());
        assertThat(userAndRoom1).isEqualTo(userAndRoom2);
        userAndRoom2.setId(2L);
        assertThat(userAndRoom1).isNotEqualTo(userAndRoom2);
        userAndRoom1.setId(null);
        assertThat(userAndRoom1).isNotEqualTo(userAndRoom2);
    }
}
