package asc.foods.order.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.StoreType} entity.
 */
public class StoreTypeDTO implements Serializable {

    private Long id;

    private String type;

    private String imageUrl;

    private String description;

    private Instant creationDate;

    private String createdBy;

    private String cardColor;

    private String backgroundImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreTypeDTO)) {
            return false;
        }

        StoreTypeDTO storeTypeDTO = (StoreTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreTypeDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", cardColor='" + getCardColor() + "'" +
            ", backgroundImage='" + getBackgroundImage() + "'" +
            "}";
    }
}
