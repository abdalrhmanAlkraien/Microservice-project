package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.PriceRange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AscStore.
 */
@Entity
@Table(name = "asc_store")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AscStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ad_video_url")
    private String adVideoUrl;

    @Column(name = "store_image_url")
    private String storeImageUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_rating")
    private Long totalRating;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "has_favorite")
    private Boolean hasFavorite;

    @Column(name = "min_order")
    private Long minOrder;

    @Column(name = "delivery")
    private Double delivery;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_range")
    private PriceRange priceRange;

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orders", "productBranches", "branchDeliveryMethods", "store" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "productOptions", "orders", "userProducts", "itemTypes", "orderCustomes", "productBranches", "store", "foodGenre", "productTag",
        },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "store" }, allowSetters = true)
    private Set<PromoCode> promoCodes = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "store", "appUser" }, allowSetters = true)
    private Set<Story> stories = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "store", "appUser" }, allowSetters = true)
    private Set<StoreFollower> followedBys = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "store" }, allowSetters = true)
    private Set<ProductTag> productTags = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_asc_store__food_geners",
        joinColumns = @JoinColumn(name = "asc_store_id"),
        inverseJoinColumns = @JoinColumn(name = "food_geners_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "storeType", "stores" }, allowSetters = true)
    private Set<FoodGenre> foodGeners = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ascStores", "foodGenres" }, allowSetters = true)
    private StoreType store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AscStore id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AscStore name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdVideoUrl() {
        return this.adVideoUrl;
    }

    public AscStore adVideoUrl(String adVideoUrl) {
        this.setAdVideoUrl(adVideoUrl);
        return this;
    }

    public void setAdVideoUrl(String adVideoUrl) {
        this.adVideoUrl = adVideoUrl;
    }

    public String getStoreImageUrl() {
        return this.storeImageUrl;
    }

    public AscStore storeImageUrl(String storeImageUrl) {
        this.setStoreImageUrl(storeImageUrl);
        return this;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getCoverImageUrl() {
        return this.coverImageUrl;
    }

    public AscStore coverImageUrl(String coverImageUrl) {
        this.setCoverImageUrl(coverImageUrl);
        return this;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public AscStore averageRating(Double averageRating) {
        this.setAverageRating(averageRating);
        return this;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRating() {
        return this.totalRating;
    }

    public AscStore totalRating(Long totalRating) {
        this.setTotalRating(totalRating);
        return this;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public AscStore creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AscStore createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getHasFavorite() {
        return this.hasFavorite;
    }

    public AscStore hasFavorite(Boolean hasFavorite) {
        this.setHasFavorite(hasFavorite);
        return this;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Long getMinOrder() {
        return this.minOrder;
    }

    public AscStore minOrder(Long minOrder) {
        this.setMinOrder(minOrder);
        return this;
    }

    public void setMinOrder(Long minOrder) {
        this.minOrder = minOrder;
    }

    public Double getDelivery() {
        return this.delivery;
    }

    public AscStore delivery(Double delivery) {
        this.setDelivery(delivery);
        return this;
    }

    public void setDelivery(Double delivery) {
        this.delivery = delivery;
    }

    public String getDescription() {
        return this.description;
    }

    public AscStore description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriceRange getPriceRange() {
        return this.priceRange;
    }

    public AscStore priceRange(PriceRange priceRange) {
        this.setPriceRange(priceRange);
        return this;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(Set<Branch> branches) {
        if (this.branches != null) {
            this.branches.forEach(i -> i.setStore(null));
        }
        if (branches != null) {
            branches.forEach(i -> i.setStore(this));
        }
        this.branches = branches;
    }

    public AscStore branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public AscStore addBranch(Branch branch) {
        this.branches.add(branch);
        branch.setStore(this);
        return this;
    }

    public AscStore removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.setStore(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setStore(null));
        }
        if (products != null) {
            products.forEach(i -> i.setStore(this));
        }
        this.products = products;
    }

    public AscStore products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public AscStore addProduct(Product product) {
        this.products.add(product);
        product.setStore(this);
        return this;
    }

    public AscStore removeProduct(Product product) {
        this.products.remove(product);
        product.setStore(null);
        return this;
    }

    public Set<PromoCode> getPromoCodes() {
        return this.promoCodes;
    }

    public void setPromoCodes(Set<PromoCode> promoCodes) {
        if (this.promoCodes != null) {
            this.promoCodes.forEach(i -> i.setStore(null));
        }
        if (promoCodes != null) {
            promoCodes.forEach(i -> i.setStore(this));
        }
        this.promoCodes = promoCodes;
    }

    public AscStore promoCodes(Set<PromoCode> promoCodes) {
        this.setPromoCodes(promoCodes);
        return this;
    }

    public AscStore addPromoCode(PromoCode promoCode) {
        this.promoCodes.add(promoCode);
        promoCode.setStore(this);
        return this;
    }

    public AscStore removePromoCode(PromoCode promoCode) {
        this.promoCodes.remove(promoCode);
        promoCode.setStore(null);
        return this;
    }

    public Set<Story> getStories() {
        return this.stories;
    }

    public void setStories(Set<Story> stories) {
        if (this.stories != null) {
            this.stories.forEach(i -> i.setStore(null));
        }
        if (stories != null) {
            stories.forEach(i -> i.setStore(this));
        }
        this.stories = stories;
    }

    public AscStore stories(Set<Story> stories) {
        this.setStories(stories);
        return this;
    }

    public AscStore addStory(Story story) {
        this.stories.add(story);
        story.setStore(this);
        return this;
    }

    public AscStore removeStory(Story story) {
        this.stories.remove(story);
        story.setStore(null);
        return this;
    }

    public Set<StoreFollower> getFollowedBys() {
        return this.followedBys;
    }

    public void setFollowedBys(Set<StoreFollower> storeFollowers) {
        if (this.followedBys != null) {
            this.followedBys.forEach(i -> i.setStore(null));
        }
        if (storeFollowers != null) {
            storeFollowers.forEach(i -> i.setStore(this));
        }
        this.followedBys = storeFollowers;
    }

    public AscStore followedBys(Set<StoreFollower> storeFollowers) {
        this.setFollowedBys(storeFollowers);
        return this;
    }

    public AscStore addFollowedBys(StoreFollower storeFollower) {
        this.followedBys.add(storeFollower);
        storeFollower.setStore(this);
        return this;
    }

    public AscStore removeFollowedBys(StoreFollower storeFollower) {
        this.followedBys.remove(storeFollower);
        storeFollower.setStore(null);
        return this;
    }

    public Set<ProductTag> getProductTags() {
        return this.productTags;
    }

    public void setProductTags(Set<ProductTag> productTags) {
        if (this.productTags != null) {
            this.productTags.forEach(i -> i.setStore(null));
        }
        if (productTags != null) {
            productTags.forEach(i -> i.setStore(this));
        }
        this.productTags = productTags;
    }

    public AscStore productTags(Set<ProductTag> productTags) {
        this.setProductTags(productTags);
        return this;
    }

    public AscStore addProductTag(ProductTag productTag) {
        this.productTags.add(productTag);
        productTag.setStore(this);
        return this;
    }

    public AscStore removeProductTag(ProductTag productTag) {
        this.productTags.remove(productTag);
        productTag.setStore(null);
        return this;
    }

    public Set<FoodGenre> getFoodGeners() {
        return this.foodGeners;
    }

    public void setFoodGeners(Set<FoodGenre> foodGenres) {
        this.foodGeners = foodGenres;
    }

    public AscStore foodGeners(Set<FoodGenre> foodGenres) {
        this.setFoodGeners(foodGenres);
        return this;
    }

    public AscStore addFoodGeners(FoodGenre foodGenre) {
        this.foodGeners.add(foodGenre);
        foodGenre.getStores().add(this);
        return this;
    }

    public AscStore removeFoodGeners(FoodGenre foodGenre) {
        this.foodGeners.remove(foodGenre);
        foodGenre.getStores().remove(this);
        return this;
    }

    public StoreType getStore() {
        return this.store;
    }

    public void setStore(StoreType storeType) {
        this.store = storeType;
    }

    public AscStore store(StoreType storeType) {
        this.setStore(storeType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AscStore)) {
            return false;
        }
        return id != null && id.equals(((AscStore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AscStore{" +
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
            "}";
    }
}
