package asc.foods.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderCustome.
 */
@Entity
@Table(name = "order_custome")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderCustome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "productOptions", "orders", "userProducts", "itemTypes", "orderCustomes", "productBranches", "store", "foodGenre", "productTag",
        },
        allowSetters = true
    )
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "ratings", "products", "orderCustomes", "store", "userAddress", "appUser", "driver" },
        allowSetters = true
    )
    private AscOrder ascOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderCustomes", "itemType" }, allowSetters = true)
    private MealCustmize mealCustmize;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderCustome id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderCustome product(Product product) {
        this.setProduct(product);
        return this;
    }

    public AscOrder getAscOrder() {
        return this.ascOrder;
    }

    public void setAscOrder(AscOrder ascOrder) {
        this.ascOrder = ascOrder;
    }

    public OrderCustome ascOrder(AscOrder ascOrder) {
        this.setAscOrder(ascOrder);
        return this;
    }

    public MealCustmize getMealCustmize() {
        return this.mealCustmize;
    }

    public void setMealCustmize(MealCustmize mealCustmize) {
        this.mealCustmize = mealCustmize;
    }

    public OrderCustome mealCustmize(MealCustmize mealCustmize) {
        this.setMealCustmize(mealCustmize);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderCustome)) {
            return false;
        }
        return id != null && id.equals(((OrderCustome) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCustome{" +
            "id=" + getId() +
            "}";
    }
}
