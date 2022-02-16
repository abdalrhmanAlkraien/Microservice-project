package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.BranchStatus;
import asc.foods.store.domain.enumeration.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Branch.
 */
@Entity
@Table(name = "branch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_rating")
    private Long totalRating;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BranchStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "tax")
    private Double tax;

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "ratings", "products", "orderCustomes", "store", "userAddress", "appUser", "driver" },
        allowSetters = true
    )
    private Set<AscOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "branch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "branch" }, allowSetters = true)
    private Set<ProductBranch> productBranches = new HashSet<>();

    @OneToMany(mappedBy = "branch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "branch" }, allowSetters = true)
    private Set<BranchDeliveryMethod> branchDeliveryMethods = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private AscStore store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Branch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Branch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Branch latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Branch longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public Branch averageRating(Double averageRating) {
        this.setAverageRating(averageRating);
        return this;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRating() {
        return this.totalRating;
    }

    public Branch totalRating(Long totalRating) {
        this.setTotalRating(totalRating);
        return this;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Branch creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Branch createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public BranchStatus getStatus() {
        return this.status;
    }

    public Branch status(BranchStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BranchStatus status) {
        this.status = status;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Branch discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountAmount() {
        return this.discountAmount;
    }

    public Branch discountAmount(Double discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTax() {
        return this.tax;
    }

    public Branch tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Set<AscOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<AscOrder> ascOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setStore(null));
        }
        if (ascOrders != null) {
            ascOrders.forEach(i -> i.setStore(this));
        }
        this.orders = ascOrders;
    }

    public Branch orders(Set<AscOrder> ascOrders) {
        this.setOrders(ascOrders);
        return this;
    }

    public Branch addOrders(AscOrder ascOrder) {
        this.orders.add(ascOrder);
        ascOrder.setStore(this);
        return this;
    }

    public Branch removeOrders(AscOrder ascOrder) {
        this.orders.remove(ascOrder);
        ascOrder.setStore(null);
        return this;
    }

    public Set<ProductBranch> getProductBranches() {
        return this.productBranches;
    }

    public void setProductBranches(Set<ProductBranch> productBranches) {
        if (this.productBranches != null) {
            this.productBranches.forEach(i -> i.setBranch(null));
        }
        if (productBranches != null) {
            productBranches.forEach(i -> i.setBranch(this));
        }
        this.productBranches = productBranches;
    }

    public Branch productBranches(Set<ProductBranch> productBranches) {
        this.setProductBranches(productBranches);
        return this;
    }

    public Branch addProductBranch(ProductBranch productBranch) {
        this.productBranches.add(productBranch);
        productBranch.setBranch(this);
        return this;
    }

    public Branch removeProductBranch(ProductBranch productBranch) {
        this.productBranches.remove(productBranch);
        productBranch.setBranch(null);
        return this;
    }

    public Set<BranchDeliveryMethod> getBranchDeliveryMethods() {
        return this.branchDeliveryMethods;
    }

    public void setBranchDeliveryMethods(Set<BranchDeliveryMethod> branchDeliveryMethods) {
        if (this.branchDeliveryMethods != null) {
            this.branchDeliveryMethods.forEach(i -> i.setBranch(null));
        }
        if (branchDeliveryMethods != null) {
            branchDeliveryMethods.forEach(i -> i.setBranch(this));
        }
        this.branchDeliveryMethods = branchDeliveryMethods;
    }

    public Branch branchDeliveryMethods(Set<BranchDeliveryMethod> branchDeliveryMethods) {
        this.setBranchDeliveryMethods(branchDeliveryMethods);
        return this;
    }

    public Branch addBranchDeliveryMethod(BranchDeliveryMethod branchDeliveryMethod) {
        this.branchDeliveryMethods.add(branchDeliveryMethod);
        branchDeliveryMethod.setBranch(this);
        return this;
    }

    public Branch removeBranchDeliveryMethod(BranchDeliveryMethod branchDeliveryMethod) {
        this.branchDeliveryMethods.remove(branchDeliveryMethod);
        branchDeliveryMethod.setBranch(null);
        return this;
    }

    public AscStore getStore() {
        return this.store;
    }

    public void setStore(AscStore ascStore) {
        this.store = ascStore;
    }

    public Branch store(AscStore ascStore) {
        this.setStore(ascStore);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }
        return id != null && id.equals(((Branch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Branch{" +
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
            "}";
    }
}
