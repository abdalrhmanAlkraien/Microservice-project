package asc.foods.order.domain;

import asc.foods.order.domain.enumeration.DeliveryMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BranchDeliveryMethod.
 */
@Entity
@Table(name = "branch_delivery_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BranchDeliveryMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_metod")
    private DeliveryMethod deliveryMetod;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "productBranches", "branchDeliveryMethods", "store" }, allowSetters = true)
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BranchDeliveryMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryMethod getDeliveryMetod() {
        return this.deliveryMetod;
    }

    public BranchDeliveryMethod deliveryMetod(DeliveryMethod deliveryMetod) {
        this.setDeliveryMetod(deliveryMetod);
        return this;
    }

    public void setDeliveryMetod(DeliveryMethod deliveryMetod) {
        this.deliveryMetod = deliveryMetod;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BranchDeliveryMethod branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchDeliveryMethod)) {
            return false;
        }
        return id != null && id.equals(((BranchDeliveryMethod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchDeliveryMethod{" +
            "id=" + getId() +
            ", deliveryMetod='" + getDeliveryMetod() + "'" +
            "}";
    }
}
