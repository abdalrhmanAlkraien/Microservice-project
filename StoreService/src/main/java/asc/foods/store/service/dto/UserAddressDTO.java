package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.AddressType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.UserAddress} entity.
 */
public class UserAddressDTO implements Serializable {

    private Long id;

    private String locationDescription;

    private Double latitude;

    private Double longitude;

    private String area;

    private String street;

    private Long flatNo;

    private String buildingName;

    private String villaNo;

    private AddressType addressType;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(Long flatNo) {
        this.flatNo = flatNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getVillaNo() {
        return villaNo;
    }

    public void setVillaNo(String villaNo) {
        this.villaNo = villaNo;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAddressDTO)) {
            return false;
        }

        UserAddressDTO userAddressDTO = (UserAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAddressDTO{" +
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
            ", appUser=" + getAppUser() +
            "}";
    }
}
