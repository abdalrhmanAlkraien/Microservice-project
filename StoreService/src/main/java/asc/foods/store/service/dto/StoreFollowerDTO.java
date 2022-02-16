package asc.foods.store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.StoreFollower} entity.
 */
public class StoreFollowerDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private AscStoreDTO store;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public AscStoreDTO getStore() {
        return store;
    }

    public void setStore(AscStoreDTO store) {
        this.store = store;
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
        if (!(o instanceof StoreFollowerDTO)) {
            return false;
        }

        StoreFollowerDTO storeFollowerDTO = (StoreFollowerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeFollowerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreFollowerDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", store=" + getStore() +
            ", appUser=" + getAppUser() +
            "}";
    }
}
