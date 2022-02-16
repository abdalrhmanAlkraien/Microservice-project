package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.BranchStatus;
import asc.foods.order.domain.enumeration.DiscountType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.Branch} entity.
 */
public class BranchDTO implements Serializable {

    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    private Double averageRating;

    private Long totalRating;

    private Instant creationDate;

    private String createdBy;

    private BranchStatus status;

    private DiscountType discountType;

    private Double discountAmount;

    private Double tax;

    private AscStoreDTO store;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public BranchStatus getStatus() {
        return status;
    }

    public void setStatus(BranchStatus status) {
        this.status = status;
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

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public AscStoreDTO getStore() {
        return store;
    }

    public void setStore(AscStoreDTO store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchDTO)) {
            return false;
        }

        BranchDTO branchDTO = (BranchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", averageRating=" + getAverageRating() +
            ", totalRating=" + getTotalRating() +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountAmount=" + getDiscountAmount() +
            ", tax=" + getTax() +
            ", store=" + getStore() +
            "}";
    }
}
