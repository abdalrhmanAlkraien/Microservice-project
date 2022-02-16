package asc.foods.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProduct.
 */
@Entity
@Table(name = "user_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "ordered_times")
    private Long orderedTimes;

    @Column(name = "last_time_ordered")
    private Instant lastTimeOrdered;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "orders",
            "ratings",
            "userAddresses",
            "follows",
            "userProducts",
            "posts",
            "likePosts",
            "comments",
            "likeComments",
            "saves",
            "stories",
            "userAndRooms",
            "readBies",
            "friends",
            "viwedStories",
            "driver",
            "friendOfs",
        },
        allowSetters = true
    )
    private AppUser appUser;

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

    public UserProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsFavorite() {
        return this.isFavorite;
    }

    public UserProduct isFavorite(Boolean isFavorite) {
        this.setIsFavorite(isFavorite);
        return this;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Long getOrderedTimes() {
        return this.orderedTimes;
    }

    public UserProduct orderedTimes(Long orderedTimes) {
        this.setOrderedTimes(orderedTimes);
        return this;
    }

    public void setOrderedTimes(Long orderedTimes) {
        this.orderedTimes = orderedTimes;
    }

    public Instant getLastTimeOrdered() {
        return this.lastTimeOrdered;
    }

    public UserProduct lastTimeOrdered(Instant lastTimeOrdered) {
        this.setLastTimeOrdered(lastTimeOrdered);
        return this;
    }

    public void setLastTimeOrdered(Instant lastTimeOrdered) {
        this.lastTimeOrdered = lastTimeOrdered;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public UserProduct appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserProduct product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProduct)) {
            return false;
        }
        return id != null && id.equals(((UserProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProduct{" +
            "id=" + getId() +
            ", isFavorite='" + getIsFavorite() + "'" +
            ", orderedTimes=" + getOrderedTimes() +
            ", lastTimeOrdered='" + getLastTimeOrdered() + "'" +
            "}";
    }
}
