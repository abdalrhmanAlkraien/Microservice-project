package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.DriverStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Driver.
 */
@Entity
@Table(name = "driver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "total_trips")
    private Integer totalTrips;

    @Column(name = "average_trip_time")
    private Instant averageTripTime;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_rating")
    private Long totalRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DriverStatus status;

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
    @OneToOne
    @JoinColumn(unique = true)
    private AppUser appUser;

    @OneToMany(mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "ratings", "products", "orderCustomes", "store", "userAddress", "appUser", "driver" },
        allowSetters = true
    )
    private Set<AscOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "driver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "driver", "order" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Driver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Driver latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Driver longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalTrips() {
        return this.totalTrips;
    }

    public Driver totalTrips(Integer totalTrips) {
        this.setTotalTrips(totalTrips);
        return this;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Instant getAverageTripTime() {
        return this.averageTripTime;
    }

    public Driver averageTripTime(Instant averageTripTime) {
        this.setAverageTripTime(averageTripTime);
        return this;
    }

    public void setAverageTripTime(Instant averageTripTime) {
        this.averageTripTime = averageTripTime;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public Driver averageRating(Double averageRating) {
        this.setAverageRating(averageRating);
        return this;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRating() {
        return this.totalRating;
    }

    public Driver totalRating(Long totalRating) {
        this.setTotalRating(totalRating);
        return this;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public DriverStatus getStatus() {
        return this.status;
    }

    public Driver status(DriverStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Driver appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<AscOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<AscOrder> ascOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setDriver(null));
        }
        if (ascOrders != null) {
            ascOrders.forEach(i -> i.setDriver(this));
        }
        this.orders = ascOrders;
    }

    public Driver orders(Set<AscOrder> ascOrders) {
        this.setOrders(ascOrders);
        return this;
    }

    public Driver addOrders(AscOrder ascOrder) {
        this.orders.add(ascOrder);
        ascOrder.setDriver(this);
        return this;
    }

    public Driver removeOrders(AscOrder ascOrder) {
        this.orders.remove(ascOrder);
        ascOrder.setDriver(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setDriver(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setDriver(this));
        }
        this.ratings = ratings;
    }

    public Driver ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Driver addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setDriver(this);
        return this;
    }

    public Driver removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setDriver(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Driver)) {
            return false;
        }
        return id != null && id.equals(((Driver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Driver{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", totalTrips=" + getTotalTrips() +
            ", averageTripTime='" + getAverageTripTime() + "'" +
            ", averageRating=" + getAverageRating() +
            ", totalRating=" + getTotalRating() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
