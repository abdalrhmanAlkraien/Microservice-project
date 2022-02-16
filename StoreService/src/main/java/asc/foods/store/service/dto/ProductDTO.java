package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.DiscountType;
import asc.foods.store.domain.enumeration.MealStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private Double price;

    private Instant creationDate;

    private String createdBy;

    private Long orderTimes;

    private Boolean hasFavorite;

    private DiscountType discountType;

    private Double discountAmount;

    private MealStatus mealStatus;

    private AscStoreDTO store;

    private FoodGenreDTO foodGenre;

    private ProductTagDTO productTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Long getOrderTimes() {
        return orderTimes;
    }

    public void setOrderTimes(Long orderTimes) {
        this.orderTimes = orderTimes;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public MealStatus getMealStatus() {
        return mealStatus;
    }

    public void setMealStatus(MealStatus mealStatus) {
        this.mealStatus = mealStatus;
    }

    public AscStoreDTO getStore() {
        return store;
    }

    public void setStore(AscStoreDTO store) {
        this.store = store;
    }

    public FoodGenreDTO getFoodGenre() {
        return foodGenre;
    }

    public void setFoodGenre(FoodGenreDTO foodGenre) {
        this.foodGenre = foodGenre;
    }

    public ProductTagDTO getProductTag() {
        return productTag;
    }

    public void setProductTag(ProductTagDTO productTag) {
        this.productTag = productTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", price=" + getPrice() +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", orderTimes=" + getOrderTimes() +
            ", hasFavorite='" + getHasFavorite() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountAmount=" + getDiscountAmount() +
            ", mealStatus='" + getMealStatus() + "'" +
            ", store=" + getStore() +
            ", foodGenre=" + getFoodGenre() +
            ", productTag=" + getProductTag() +
            "}";
    }
}
