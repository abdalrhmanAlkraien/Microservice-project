package asc.foods.store.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.ReadBy} entity.
 */
public class ReadByDTO implements Serializable {

    private Long id;

    private AppUserDTO appUser;

    private MessageDTO message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public MessageDTO getMessage() {
        return message;
    }

    public void setMessage(MessageDTO message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReadByDTO)) {
            return false;
        }

        ReadByDTO readByDTO = (ReadByDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, readByDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReadByDTO{" +
            "id=" + getId() +
            ", appUser=" + getAppUser() +
            ", message=" + getMessage() +
            "}";
    }
}
