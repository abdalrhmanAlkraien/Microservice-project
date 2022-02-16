package asc.foods.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StoreType.
 */
@Entity
@Table(name = "store_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StoreType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "card_color")
    private String cardColor;

    @Column(name = "background_image")
    private String backgroundImage;

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private Set<AscStore> ascStores = new HashSet<>();

    @OneToMany(mappedBy = "storeType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "storeType", "stores" }, allowSetters = true)
    private Set<FoodGenre> foodGenres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StoreType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public StoreType type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public StoreType imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public StoreType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public StoreType creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public StoreType createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCardColor() {
        return this.cardColor;
    }

    public StoreType cardColor(String cardColor) {
        this.setCardColor(cardColor);
        return this;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getBackgroundImage() {
        return this.backgroundImage;
    }

    public StoreType backgroundImage(String backgroundImage) {
        this.setBackgroundImage(backgroundImage);
        return this;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Set<AscStore> getAscStores() {
        return this.ascStores;
    }

    public void setAscStores(Set<AscStore> ascStores) {
        if (this.ascStores != null) {
            this.ascStores.forEach(i -> i.setStore(null));
        }
        if (ascStores != null) {
            ascStores.forEach(i -> i.setStore(this));
        }
        this.ascStores = ascStores;
    }

    public StoreType ascStores(Set<AscStore> ascStores) {
        this.setAscStores(ascStores);
        return this;
    }

    public StoreType addAscStore(AscStore ascStore) {
        this.ascStores.add(ascStore);
        ascStore.setStore(this);
        return this;
    }

    public StoreType removeAscStore(AscStore ascStore) {
        this.ascStores.remove(ascStore);
        ascStore.setStore(null);
        return this;
    }

    public Set<FoodGenre> getFoodGenres() {
        return this.foodGenres;
    }

    public void setFoodGenres(Set<FoodGenre> foodGenres) {
        if (this.foodGenres != null) {
            this.foodGenres.forEach(i -> i.setStoreType(null));
        }
        if (foodGenres != null) {
            foodGenres.forEach(i -> i.setStoreType(this));
        }
        this.foodGenres = foodGenres;
    }

    public StoreType foodGenres(Set<FoodGenre> foodGenres) {
        this.setFoodGenres(foodGenres);
        return this;
    }

    public StoreType addFoodGenre(FoodGenre foodGenre) {
        this.foodGenres.add(foodGenre);
        foodGenre.setStoreType(this);
        return this;
    }

    public StoreType removeFoodGenre(FoodGenre foodGenre) {
        this.foodGenres.remove(foodGenre);
        foodGenre.setStoreType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreType)) {
            return false;
        }
        return id != null && id.equals(((StoreType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreType{" +
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
