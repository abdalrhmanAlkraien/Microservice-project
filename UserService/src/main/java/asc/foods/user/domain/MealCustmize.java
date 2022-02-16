package asc.foods.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MealCustmize.
 */
@Entity
@Table(name = "meal_custmize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MealCustmize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "order_number")
    private Long orderNumber;

    @OneToMany(mappedBy = "mealCustmize")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "ascOrder", "mealCustmize" }, allowSetters = true)
    private Set<OrderCustome> orderCustomes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "mealCustmizes", "product" }, allowSetters = true)
    private ItemType itemType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MealCustmize id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public MealCustmize title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return this.price;
    }

    public MealCustmize price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public MealCustmize imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOrderNumber() {
        return this.orderNumber;
    }

    public MealCustmize orderNumber(Long orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<OrderCustome> getOrderCustomes() {
        return this.orderCustomes;
    }

    public void setOrderCustomes(Set<OrderCustome> orderCustomes) {
        if (this.orderCustomes != null) {
            this.orderCustomes.forEach(i -> i.setMealCustmize(null));
        }
        if (orderCustomes != null) {
            orderCustomes.forEach(i -> i.setMealCustmize(this));
        }
        this.orderCustomes = orderCustomes;
    }

    public MealCustmize orderCustomes(Set<OrderCustome> orderCustomes) {
        this.setOrderCustomes(orderCustomes);
        return this;
    }

    public MealCustmize addOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.add(orderCustome);
        orderCustome.setMealCustmize(this);
        return this;
    }

    public MealCustmize removeOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.remove(orderCustome);
        orderCustome.setMealCustmize(null);
        return this;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public MealCustmize itemType(ItemType itemType) {
        this.setItemType(itemType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealCustmize)) {
            return false;
        }
        return id != null && id.equals(((MealCustmize) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealCustmize{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", orderNumber=" + getOrderNumber() +
            "}";
    }
}
