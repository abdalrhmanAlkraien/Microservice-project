package asc.foods.store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.ItemType} entity.
 */
public class ItemTypeDTO implements Serializable {

    private Long id;

    private String title;

    private String decription;

    private String imageUrl;

    private Instant creationDate;

    private String creationBy;

    private Boolean increase;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationBy() {
        return creationBy;
    }

    public void setCreationBy(String creationBy) {
        this.creationBy = creationBy;
    }

    public Boolean getIncrease() {
        return increase;
    }

    public void setIncrease(Boolean increase) {
        this.increase = increase;
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
        if (!(o instanceof ItemTypeDTO)) {
            return false;
        }

        ItemTypeDTO itemTypeDTO = (ItemTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTypeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", decription='" + getDecription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", creationBy='" + getCreationBy() + "'" +
            ", increase='" + getIncrease() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
