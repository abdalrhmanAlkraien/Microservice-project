package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.DriverStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.Driver} entity.
 */
public class DriverDTO implements Serializable {

    private Long id;

    private Double latitude;

    private Double longitude;

    private Integer totalTrips;

    private Instant averageTripTime;

    private Double averageRating;

    private Long totalRating;

    private DriverStatus status;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Instant getAverageTripTime() {
        return averageTripTime;
    }

    public void setAverageTripTime(Instant averageTripTime) {
        this.averageTripTime = averageTripTime;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
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
        if (!(o instanceof DriverDTO)) {
            return false;
        }

        DriverDTO driverDTO = (DriverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, driverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", totalTrips=" + getTotalTrips() +
            ", averageTripTime='" + getAverageTripTime() + "'" +
            ", averageRating=" + getAverageRating() +
            ", totalRating=" + getTotalRating() +
            ", status='" + getStatus() + "'" +
            ", appUser=" + getAppUser() +
            "}";
    }
}
