package asc.foods.user.service.dto;

import asc.foods.user.domain.enumeration.PriceRange;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link asc.foods.user.domain.AscStore} entity.
 */
public class AscStoreDTO implements Serializable {

    private Long id;

    private String name;

    private String adVideoUrl;

    private String storeImageUrl;

    private String coverImageUrl;

    private Double averageRating;

    private Long totalRating;

    private Instant creationDate;

    private String createdBy;

    private Boolean hasFavorite;

    private Long minOrder;

    private Double delivery;

    private String description;

    private PriceRange priceRange;

    private Set<FoodGenreDTO> foodGeners = new HashSet<>();

    private StoreTypeDTO store;

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

    public String getAdVideoUrl() {
        return adVideoUrl;
    }

    public void setAdVideoUrl(String adVideoUrl) {
        this.adVideoUrl = adVideoUrl;
    }

    public String getStoreImageUrl() {
        return storeImageUrl;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
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

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Long getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(Long minOrder) {
        this.minOrder = minOrder;
    }

    public Double getDelivery() {
        return delivery;
    }

    public void setDelivery(Double delivery) {
        this.delivery = delivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Set<FoodGenreDTO> getFoodGeners() {
        return foodGeners;
    }

    public void setFoodGeners(Set<FoodGenreDTO> foodGeners) {
        this.foodGeners = foodGeners;
    }

    public StoreTypeDTO getStore() {
        return store;
    }

    public void setStore(StoreTypeDTO store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AscStoreDTO)) {
            return false;
        }

        AscStoreDTO ascStoreDTO = (AscStoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ascStoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AscStoreDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", adVideoUrl='" + getAdVideoUrl() + "'" +
            ", storeImageUrl='" + getStoreImageUrl() + "'" +
            ", coverImageUrl='" + getCoverImageUrl() + "'" +
            ", averageRating=" + getAverageRating() +
            ", totalRating=" + getTotalRating() +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", hasFavorite='" + getHasFavorite() + "'" +
            ", minOrder=" + getMinOrder() +
            ", delivery=" + getDelivery() +
            ", description='" + getDescription() + "'" +
            ", priceRange='" + getPriceRange() + "'" +
            ", foodGeners=" + getFoodGeners() +
            ", store=" + getStore() +
            "}";
    }
}
