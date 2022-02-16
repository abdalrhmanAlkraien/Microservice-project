package asc.foods.order.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.MealCustmize} entity.
 */
public class MealCustmizeDTO implements Serializable {

    private Long id;

    private String title;

    private Double price;

    private String imageUrl;

    private Long orderNumber;

    private ItemTypeDTO itemType;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ItemTypeDTO getItemType() {
        return itemType;
    }

    public void setItemType(ItemTypeDTO itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealCustmizeDTO)) {
            return false;
        }

        MealCustmizeDTO mealCustmizeDTO = (MealCustmizeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mealCustmizeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealCustmizeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", orderNumber=" + getOrderNumber() +
            ", itemType=" + getItemType() +
            "}";
    }
}
