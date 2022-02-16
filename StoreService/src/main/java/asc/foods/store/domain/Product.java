package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.DiscountType;
import asc.foods.store.domain.enumeration.MealStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price")
    private Double price;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "order_times")
    private Long orderTimes;

    @Column(name = "has_favorite")
    private Boolean hasFavorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_status")
    private MealStatus mealStatus;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductOption> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order", "product" }, allowSetters = true)
    private Set<OrderProduct> orders = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "product" }, allowSetters = true)
    private Set<UserProduct> userProducts = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mealCustmizes", "product" }, allowSetters = true)
    private Set<ItemType> itemTypes = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "ascOrder", "mealCustmize" }, allowSetters = true)
    private Set<OrderCustome> orderCustomes = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "branch" }, allowSetters = true)
    private Set<ProductBranch> productBranches = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private AscStore store;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "storeType", "stores" }, allowSetters = true)
    private FoodGenre foodGenre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "store" }, allowSetters = true)
    private ProductTag productTag;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return this.price;
    }

    public Product price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Product creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getOrderTimes() {
        return this.orderTimes;
    }

    public Product orderTimes(Long orderTimes) {
        this.setOrderTimes(orderTimes);
        return this;
    }

    public void setOrderTimes(Long orderTimes) {
        this.orderTimes = orderTimes;
    }

    public Boolean getHasFavorite() {
        return this.hasFavorite;
    }

    public Product hasFavorite(Boolean hasFavorite) {
        this.setHasFavorite(hasFavorite);
        return this;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Product discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountAmount() {
        return this.discountAmount;
    }

    public Product discountAmount(Double discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public MealStatus getMealStatus() {
        return this.mealStatus;
    }

    public Product mealStatus(MealStatus mealStatus) {
        this.setMealStatus(mealStatus);
        return this;
    }

    public void setMealStatus(MealStatus mealStatus) {
        this.mealStatus = mealStatus;
    }

    public Set<ProductOption> getProductOptions() {
        return this.productOptions;
    }

    public void setProductOptions(Set<ProductOption> productOptions) {
        if (this.productOptions != null) {
            this.productOptions.forEach(i -> i.setProduct(null));
        }
        if (productOptions != null) {
            productOptions.forEach(i -> i.setProduct(this));
        }
        this.productOptions = productOptions;
    }

    public Product productOptions(Set<ProductOption> productOptions) {
        this.setProductOptions(productOptions);
        return this;
    }

    public Product addProductOption(ProductOption productOption) {
        this.productOptions.add(productOption);
        productOption.setProduct(this);
        return this;
    }

    public Product removeProductOption(ProductOption productOption) {
        this.productOptions.remove(productOption);
        productOption.setProduct(null);
        return this;
    }

    public Set<OrderProduct> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<OrderProduct> orderProducts) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setProduct(null));
        }
        if (orderProducts != null) {
            orderProducts.forEach(i -> i.setProduct(this));
        }
        this.orders = orderProducts;
    }

    public Product orders(Set<OrderProduct> orderProducts) {
        this.setOrders(orderProducts);
        return this;
    }

    public Product addOrders(OrderProduct orderProduct) {
        this.orders.add(orderProduct);
        orderProduct.setProduct(this);
        return this;
    }

    public Product removeOrders(OrderProduct orderProduct) {
        this.orders.remove(orderProduct);
        orderProduct.setProduct(null);
        return this;
    }

    public Set<UserProduct> getUserProducts() {
        return this.userProducts;
    }

    public void setUserProducts(Set<UserProduct> userProducts) {
        if (this.userProducts != null) {
            this.userProducts.forEach(i -> i.setProduct(null));
        }
        if (userProducts != null) {
            userProducts.forEach(i -> i.setProduct(this));
        }
        this.userProducts = userProducts;
    }

    public Product userProducts(Set<UserProduct> userProducts) {
        this.setUserProducts(userProducts);
        return this;
    }

    public Product addUserProduct(UserProduct userProduct) {
        this.userProducts.add(userProduct);
        userProduct.setProduct(this);
        return this;
    }

    public Product removeUserProduct(UserProduct userProduct) {
        this.userProducts.remove(userProduct);
        userProduct.setProduct(null);
        return this;
    }

    public Set<ItemType> getItemTypes() {
        return this.itemTypes;
    }

    public void setItemTypes(Set<ItemType> itemTypes) {
        if (this.itemTypes != null) {
            this.itemTypes.forEach(i -> i.setProduct(null));
        }
        if (itemTypes != null) {
            itemTypes.forEach(i -> i.setProduct(this));
        }
        this.itemTypes = itemTypes;
    }

    public Product itemTypes(Set<ItemType> itemTypes) {
        this.setItemTypes(itemTypes);
        return this;
    }

    public Product addItemType(ItemType itemType) {
        this.itemTypes.add(itemType);
        itemType.setProduct(this);
        return this;
    }

    public Product removeItemType(ItemType itemType) {
        this.itemTypes.remove(itemType);
        itemType.setProduct(null);
        return this;
    }

    public Set<OrderCustome> getOrderCustomes() {
        return this.orderCustomes;
    }

    public void setOrderCustomes(Set<OrderCustome> orderCustomes) {
        if (this.orderCustomes != null) {
            this.orderCustomes.forEach(i -> i.setProduct(null));
        }
        if (orderCustomes != null) {
            orderCustomes.forEach(i -> i.setProduct(this));
        }
        this.orderCustomes = orderCustomes;
    }

    public Product orderCustomes(Set<OrderCustome> orderCustomes) {
        this.setOrderCustomes(orderCustomes);
        return this;
    }

    public Product addOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.add(orderCustome);
        orderCustome.setProduct(this);
        return this;
    }

    public Product removeOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.remove(orderCustome);
        orderCustome.setProduct(null);
        return this;
    }

    public Set<ProductBranch> getProductBranches() {
        return this.productBranches;
    }

    public void setProductBranches(Set<ProductBranch> productBranches) {
        if (this.productBranches != null) {
            this.productBranches.forEach(i -> i.setProduct(null));
        }
        if (productBranches != null) {
            productBranches.forEach(i -> i.setProduct(this));
        }
        this.productBranches = productBranches;
    }

    public Product productBranches(Set<ProductBranch> productBranches) {
        this.setProductBranches(productBranches);
        return this;
    }

    public Product addProductBranch(ProductBranch productBranch) {
        this.productBranches.add(productBranch);
        productBranch.setProduct(this);
        return this;
    }

    public Product removeProductBranch(ProductBranch productBranch) {
        this.productBranches.remove(productBranch);
        productBranch.setProduct(null);
        return this;
    }

    public AscStore getStore() {
        return this.store;
    }

    public void setStore(AscStore ascStore) {
        this.store = ascStore;
    }

    public Product store(AscStore ascStore) {
        this.setStore(ascStore);
        return this;
    }

    public FoodGenre getFoodGenre() {
        return this.foodGenre;
    }

    public void setFoodGenre(FoodGenre foodGenre) {
        this.foodGenre = foodGenre;
    }

    public Product foodGenre(FoodGenre foodGenre) {
        this.setFoodGenre(foodGenre);
        return this;
    }

    public ProductTag getProductTag() {
        return this.productTag;
    }

    public void setProductTag(ProductTag productTag) {
        this.productTag = productTag;
    }

    public Product productTag(ProductTag productTag) {
        this.setProductTag(productTag);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
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
            "}";
    }
}
