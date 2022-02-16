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
 * A ItemType.
 */
@Entity
@Table(name = "item_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "decription")
    private String decription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "creation_by")
    private String creationBy;

    @Column(name = "increase")
    private Boolean increase;

    @OneToMany(mappedBy = "itemType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orderCustomes", "itemType" }, allowSetters = true)
    private Set<MealCustmize> mealCustmizes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "productOptions", "orders", "userProducts", "itemTypes", "orderCustomes", "productBranches", "store", "foodGenre", "productTag",
        },
        allowSetters = true
    )
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ItemType title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return this.decription;
    }

    public ItemType decription(String decription) {
        this.setDecription(decription);
        return this;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public ItemType imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ItemType creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationBy() {
        return this.creationBy;
    }

    public ItemType creationBy(String creationBy) {
        this.setCreationBy(creationBy);
        return this;
    }

    public void setCreationBy(String creationBy) {
        this.creationBy = creationBy;
    }

    public Boolean getIncrease() {
        return this.increase;
    }

    public ItemType increase(Boolean increase) {
        this.setIncrease(increase);
        return this;
    }

    public void setIncrease(Boolean increase) {
        this.increase = increase;
    }

    public Set<MealCustmize> getMealCustmizes() {
        return this.mealCustmizes;
    }

    public void setMealCustmizes(Set<MealCustmize> mealCustmizes) {
        if (this.mealCustmizes != null) {
            this.mealCustmizes.forEach(i -> i.setItemType(null));
        }
        if (mealCustmizes != null) {
            mealCustmizes.forEach(i -> i.setItemType(this));
        }
        this.mealCustmizes = mealCustmizes;
    }

    public ItemType mealCustmizes(Set<MealCustmize> mealCustmizes) {
        this.setMealCustmizes(mealCustmizes);
        return this;
    }

    public ItemType addMealCustmize(MealCustmize mealCustmize) {
        this.mealCustmizes.add(mealCustmize);
        mealCustmize.setItemType(this);
        return this;
    }

    public ItemType removeMealCustmize(MealCustmize mealCustmize) {
        this.mealCustmizes.remove(mealCustmize);
        mealCustmize.setItemType(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ItemType product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemType)) {
            return false;
        }
        return id != null && id.equals(((ItemType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemType{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", decription='" + getDecription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", creationBy='" + getCreationBy() + "'" +
            ", increase='" + getIncrease() + "'" +
            "}";
    }
}
