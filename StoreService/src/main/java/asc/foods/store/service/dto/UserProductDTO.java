package asc.foods.store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.UserProduct} entity.
 */
public class UserProductDTO implements Serializable {

    private Long id;

    private Boolean isFavorite;

    private Long orderedTimes;

    private Instant lastTimeOrdered;

    private AppUserDTO appUser;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Long getOrderedTimes() {
        return orderedTimes;
    }

    public void setOrderedTimes(Long orderedTimes) {
        this.orderedTimes = orderedTimes;
    }

    public Instant getLastTimeOrdered() {
        return lastTimeOrdered;
    }

    public void setLastTimeOrdered(Instant lastTimeOrdered) {
        this.lastTimeOrdered = lastTimeOrdered;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProductDTO)) {
            return false;
        }

        UserProductDTO userProductDTO = (UserProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProductDTO{" +
            "id=" + getId() +
            ", isFavorite='" + getIsFavorite() + "'" +
            ", orderedTimes=" + getOrderedTimes() +
            ", lastTimeOrdered='" + getLastTimeOrdered() + "'" +
            ", appUser=" + getAppUser() +
            ", product=" + getProduct() +
            "}";
    }
}
