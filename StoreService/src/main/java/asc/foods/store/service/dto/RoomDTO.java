package asc.foods.store.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.Room} entity.
 */
public class RoomDTO implements Serializable {

    private Long id;

    private Long userSender;

    private Long userReciver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserSender() {
        return userSender;
    }

    public void setUserSender(Long userSender) {
        this.userSender = userSender;
    }

    public Long getUserReciver() {
        return userReciver;
    }

    public void setUserReciver(Long userReciver) {
        this.userReciver = userReciver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDTO)) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDTO{" +
            "id=" + getId() +
            ", userSender=" + getUserSender() +
            ", userReciver=" + getUserReciver() +
            "}";
    }
}
