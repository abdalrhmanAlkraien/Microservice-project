package asc.foods.user.domain;

import asc.foods.user.domain.enumeration.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAddress.
 */
@Entity
@Table(name = "user_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location_description")
    private String locationDescription;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "area")
    private String area;

    @Column(name = "street")
    private String street;

    @Column(name = "flat_no")
    private Long flatNo;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "villa_no")
    private String villaNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @OneToMany(mappedBy = "userAddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "ratings", "products", "orderCustomes", "store", "userAddress", "appUser", "driver" },
        allowSetters = true
    )
    private Set<AscOrder> orders = new HashSet<>();

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationDescription() {
        return this.locationDescription;
    }

    public UserAddress locationDescription(String locationDescription) {
        this.setLocationDescription(locationDescription);
        return this;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public UserAddress latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public UserAddress longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getArea() {
        return this.area;
    }

    public UserAddress area(String area) {
        this.setArea(area);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return this.street;
    }

    public UserAddress street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getFlatNo() {
        return this.flatNo;
    }

    public UserAddress flatNo(Long flatNo) {
        this.setFlatNo(flatNo);
        return this;
    }

    public void setFlatNo(Long flatNo) {
        this.flatNo = flatNo;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public UserAddress buildingName(String buildingName) {
        this.setBuildingName(buildingName);
        return this;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getVillaNo() {
        return this.villaNo;
    }

    public UserAddress villaNo(String villaNo) {
        this.setVillaNo(villaNo);
        return this;
    }

    public void setVillaNo(String villaNo) {
        this.villaNo = villaNo;
    }

    public AddressType getAddressType() {
        return this.addressType;
    }

    public UserAddress addressType(AddressType addressType) {
        this.setAddressType(addressType);
        return this;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public Set<AscOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<AscOrder> ascOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setUserAddress(null));
        }
        if (ascOrders != null) {
            ascOrders.forEach(i -> i.setUserAddress(this));
        }
        this.orders = ascOrders;
    }

    public UserAddress orders(Set<AscOrder> ascOrders) {
        this.setOrders(ascOrders);
        return this;
    }

    public UserAddress addOrders(AscOrder ascOrder) {
        this.orders.add(ascOrder);
        ascOrder.setUserAddress(this);
        return this;
    }

    public UserAddress removeOrders(AscOrder ascOrder) {
        this.orders.remove(ascOrder);
        ascOrder.setUserAddress(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public UserAddress appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAddress)) {
            return false;
        }
        return id != null && id.equals(((UserAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAddress{" +
            "id=" + getId() +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", area='" + getArea() + "'" +
            ", street='" + getStreet() + "'" +
            ", flatNo=" + getFlatNo() +
            ", buildingName='" + getBuildingName() + "'" +
            ", villaNo='" + getVillaNo() + "'" +
            ", addressType='" + getAddressType() + "'" +
            "}";
    }
}
