package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.OrderStatus;
import asc.foods.store.domain.enumeration.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AscOrder.
 */
@Entity
@Table(name = "asc_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AscOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "special_request")
    private String specialRequest;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "price")
    private Double price;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "tax_price")
    private Double taxPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private Payment paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "estimation_delivery_time")
    private Instant estimationDeliveryTime;

    @Column(name = "actual_delivery_time")
    private Instant actualDeliveryTime;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "driver", "order" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order", "product" }, allowSetters = true)
    private Set<OrderProduct> products = new HashSet<>();

    @OneToMany(mappedBy = "ascOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "ascOrder", "mealCustmize" }, allowSetters = true)
    private Set<OrderCustome> orderCustomes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "productBranches", "branchDeliveryMethods", "store" }, allowSetters = true)
    private Branch store;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "appUser" }, allowSetters = true)
    private UserAddress userAddress;

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
    @JsonIgnoreProperties(value = { "appUser", "orders", "ratings" }, allowSetters = true)
    private Driver driver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AscOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialRequest() {
        return this.specialRequest;
    }

    public AscOrder specialRequest(String specialRequest) {
        this.setSpecialRequest(specialRequest);
        return this;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public AscOrder latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public AscOrder longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRating() {
        return this.rating;
    }

    public AscOrder rating(Double rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getPrice() {
        return this.price;
    }

    public AscOrder price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDeliveryPrice() {
        return this.deliveryPrice;
    }

    public AscOrder deliveryPrice(Double deliveryPrice) {
        this.setDeliveryPrice(deliveryPrice);
        return this;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Double getTaxPrice() {
        return this.taxPrice;
    }

    public AscOrder taxPrice(Double taxPrice) {
        this.setTaxPrice(taxPrice);
        return this;
    }

    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Payment getPaymentMethod() {
        return this.paymentMethod;
    }

    public AscOrder paymentMethod(Payment paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public AscOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public AscOrder discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getEstimationDeliveryTime() {
        return this.estimationDeliveryTime;
    }

    public AscOrder estimationDeliveryTime(Instant estimationDeliveryTime) {
        this.setEstimationDeliveryTime(estimationDeliveryTime);
        return this;
    }

    public void setEstimationDeliveryTime(Instant estimationDeliveryTime) {
        this.estimationDeliveryTime = estimationDeliveryTime;
    }

    public Instant getActualDeliveryTime() {
        return this.actualDeliveryTime;
    }

    public AscOrder actualDeliveryTime(Instant actualDeliveryTime) {
        this.setActualDeliveryTime(actualDeliveryTime);
        return this;
    }

    public void setActualDeliveryTime(Instant actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public AscOrder creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AscOrder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setOrder(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setOrder(this));
        }
        this.ratings = ratings;
    }

    public AscOrder ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public AscOrder addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setOrder(this);
        return this;
    }

    public AscOrder removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setOrder(null);
        return this;
    }

    public Set<OrderProduct> getProducts() {
        return this.products;
    }

    public void setProducts(Set<OrderProduct> orderProducts) {
        if (this.products != null) {
            this.products.forEach(i -> i.setOrder(null));
        }
        if (orderProducts != null) {
            orderProducts.forEach(i -> i.setOrder(this));
        }
        this.products = orderProducts;
    }

    public AscOrder products(Set<OrderProduct> orderProducts) {
        this.setProducts(orderProducts);
        return this;
    }

    public AscOrder addProducts(OrderProduct orderProduct) {
        this.products.add(orderProduct);
        orderProduct.setOrder(this);
        return this;
    }

    public AscOrder removeProducts(OrderProduct orderProduct) {
        this.products.remove(orderProduct);
        orderProduct.setOrder(null);
        return this;
    }

    public Set<OrderCustome> getOrderCustomes() {
        return this.orderCustomes;
    }

    public void setOrderCustomes(Set<OrderCustome> orderCustomes) {
        if (this.orderCustomes != null) {
            this.orderCustomes.forEach(i -> i.setAscOrder(null));
        }
        if (orderCustomes != null) {
            orderCustomes.forEach(i -> i.setAscOrder(this));
        }
        this.orderCustomes = orderCustomes;
    }

    public AscOrder orderCustomes(Set<OrderCustome> orderCustomes) {
        this.setOrderCustomes(orderCustomes);
        return this;
    }

    public AscOrder addOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.add(orderCustome);
        orderCustome.setAscOrder(this);
        return this;
    }

    public AscOrder removeOrderCustome(OrderCustome orderCustome) {
        this.orderCustomes.remove(orderCustome);
        orderCustome.setAscOrder(null);
        return this;
    }

    public Branch getStore() {
        return this.store;
    }

    public void setStore(Branch branch) {
        this.store = branch;
    }

    public AscOrder store(Branch branch) {
        this.setStore(branch);
        return this;
    }

    public UserAddress getUserAddress() {
        return this.userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public AscOrder userAddress(UserAddress userAddress) {
        this.setUserAddress(userAddress);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AscOrder appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public AscOrder driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AscOrder)) {
            return false;
        }
        return id != null && id.equals(((AscOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AscOrder{" +
            "id=" + getId() +
            ", specialRequest='" + getSpecialRequest() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", rating=" + getRating() +
            ", price=" + getPrice() +
            ", deliveryPrice=" + getDeliveryPrice() +
            ", taxPrice=" + getTaxPrice() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", status='" + getStatus() + "'" +
            ", discount=" + getDiscount() +
            ", estimationDeliveryTime='" + getEstimationDeliveryTime() + "'" +
            ", actualDeliveryTime='" + getActualDeliveryTime() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
