package asc.foods.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FoodGenre.
 */
@Entity
@Table(name = "food_genre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodGenre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gener")
    private String gener;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "foodGenre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "productOptions", "orders", "userProducts", "itemTypes", "orderCustomes", "productBranches", "store", "foodGenre", "productTag",
        },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ascStores", "foodGenres" }, allowSetters = true)
    private StoreType storeType;

    @ManyToMany(mappedBy = "foodGeners")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private Set<AscStore> stores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FoodGenre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGener() {
        return this.gener;
    }

    public FoodGenre gener(String gener) {
        this.setGener(gener);
        return this;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public FoodGenre imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodGenre description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public FoodGenre creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public FoodGenre createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setFoodGenre(null));
        }
        if (products != null) {
            products.forEach(i -> i.setFoodGenre(this));
        }
        this.products = products;
    }

    public FoodGenre products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public FoodGenre addProduct(Product product) {
        this.products.add(product);
        product.setFoodGenre(this);
        return this;
    }

    public FoodGenre removeProduct(Product product) {
        this.products.remove(product);
        product.setFoodGenre(null);
        return this;
    }

    public StoreType getStoreType() {
        return this.storeType;
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType;
    }

    public FoodGenre storeType(StoreType storeType) {
        this.setStoreType(storeType);
        return this;
    }

    public Set<AscStore> getStores() {
        return this.stores;
    }

    public void setStores(Set<AscStore> ascStores) {
        if (this.stores != null) {
            this.stores.forEach(i -> i.removeFoodGeners(this));
        }
        if (ascStores != null) {
            ascStores.forEach(i -> i.addFoodGeners(this));
        }
        this.stores = ascStores;
    }

    public FoodGenre stores(Set<AscStore> ascStores) {
        this.setStores(ascStores);
        return this;
    }

    public FoodGenre addStores(AscStore ascStore) {
        this.stores.add(ascStore);
        ascStore.getFoodGeners().add(this);
        return this;
    }

    public FoodGenre removeStores(AscStore ascStore) {
        this.stores.remove(ascStore);
        ascStore.getFoodGeners().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodGenre)) {
            return false;
        }
        return id != null && id.equals(((FoodGenre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodGenre{" +
            "id=" + getId() +
            ", gener='" + getGener() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
