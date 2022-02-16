package asc.foods.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PromoCode.
 */
@Entity
@Table(name = "promo_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PromoCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "times")
    private Integer times;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "expire_date")
    private Instant expireDate;

    @Column(name = "created_by")
    private String createdBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private AscStore store;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromoCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public PromoCode code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public PromoCode description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimes() {
        return this.times;
    }

    public PromoCode times(Integer times) {
        this.setTimes(times);
        return this;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public PromoCode creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExpireDate() {
        return this.expireDate;
    }

    public PromoCode expireDate(Instant expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PromoCode createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public AscStore getStore() {
        return this.store;
    }

    public void setStore(AscStore ascStore) {
        this.store = ascStore;
    }

    public PromoCode store(AscStore ascStore) {
        this.setStore(ascStore);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoCode)) {
            return false;
        }
        return id != null && id.equals(((PromoCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoCode{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", times=" + getTimes() +
            ", creationDate='" + getCreationDate() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
