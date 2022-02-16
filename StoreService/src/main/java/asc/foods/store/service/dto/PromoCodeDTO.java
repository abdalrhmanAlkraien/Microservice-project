package asc.foods.store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.PromoCode} entity.
 */
public class PromoCodeDTO implements Serializable {

    private Long id;

    private String code;

    private String description;

    private Integer times;

    private Instant creationDate;

    private Instant expireDate;

    private String createdBy;

    private AscStoreDTO store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public AscStoreDTO getStore() {
        return store;
    }

    public void setStore(AscStoreDTO store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoCodeDTO)) {
            return false;
        }

        PromoCodeDTO promoCodeDTO = (PromoCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promoCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoCodeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", times=" + getTimes() +
            ", creationDate='" + getCreationDate() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", store=" + getStore() +
            "}";
    }
}
