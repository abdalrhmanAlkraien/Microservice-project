package asc.foods.store.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.UserAndRoom} entity.
 */
public class UserAndRoomDTO implements Serializable {

    private Long id;

    private RoomDTO room;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAndRoomDTO)) {
            return false;
        }

        UserAndRoomDTO userAndRoomDTO = (UserAndRoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAndRoomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAndRoomDTO{" +
            "id=" + getId() +
            ", room=" + getRoom() +
            ", appUser=" + getAppUser() +
            "}";
    }
}
