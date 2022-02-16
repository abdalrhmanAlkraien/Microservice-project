package asc.foods.order.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.FoodGenre} entity.
 */
public class FoodGenreDTO implements Serializable {

    private Long id;

    private String gener;

    private String imageUrl;

    private String description;

    private Instant creationDate;

    private String createdBy;

    private StoreTypeDTO storeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public StoreTypeDTO getStoreType() {
        return storeType;
    }

    public void setStoreType(StoreTypeDTO storeType) {
        this.storeType = storeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodGenreDTO)) {
            return false;
        }

        FoodGenreDTO foodGenreDTO = (FoodGenreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodGenreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodGenreDTO{" +
            "id=" + getId() +
            ", gener='" + getGener() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", storeType=" + getStoreType() +
            "}";
    }
}
