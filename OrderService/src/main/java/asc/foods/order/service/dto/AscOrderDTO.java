package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.OrderStatus;
import asc.foods.order.domain.enumeration.Payment;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.AscOrder} entity.
 */
public class AscOrderDTO implements Serializable {

    private Long id;

    private String specialRequest;

    private Double latitude;

    private Double longitude;

    private Double rating;

    private Double price;

    private Double deliveryPrice;

    private Double taxPrice;

    private Payment paymentMethod;

    private OrderStatus status;

    private Double discount;

    private Instant estimationDeliveryTime;

    private Instant actualDeliveryTime;

    private Instant creationDate;

    private String createdBy;

    private BranchDTO store;

    private UserAddressDTO userAddress;

    private AppUserDTO appUser;

    private DriverDTO driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Double getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Payment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getEstimationDeliveryTime() {
        return estimationDeliveryTime;
    }

    public void setEstimationDeliveryTime(Instant estimationDeliveryTime) {
        this.estimationDeliveryTime = estimationDeliveryTime;
    }

    public Instant getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(Instant actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public BranchDTO getStore() {
        return store;
    }

    public void setStore(BranchDTO store) {
        this.store = store;
    }

    public UserAddressDTO getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddressDTO userAddress) {
        this.userAddress = userAddress;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AscOrderDTO)) {
            return false;
        }

        AscOrderDTO ascOrderDTO = (AscOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ascOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AscOrderDTO{" +
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
            ", store=" + getStore() +
            ", userAddress=" + getUserAddress() +
            ", appUser=" + getAppUser() +
            ", driver=" + getDriver() +
            "}";
    }
}
