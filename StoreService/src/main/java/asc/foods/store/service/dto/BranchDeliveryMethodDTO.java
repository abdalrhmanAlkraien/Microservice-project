package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.DeliveryMethod;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.BranchDeliveryMethod} entity.
 */
public class BranchDeliveryMethodDTO implements Serializable {

    private Long id;

    private DeliveryMethod deliveryMetod;

    private BranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeliveryMethod getDeliveryMetod() {
        return deliveryMetod;
    }

    public void setDeliveryMetod(DeliveryMethod deliveryMetod) {
        this.deliveryMetod = deliveryMetod;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchDeliveryMethodDTO)) {
            return false;
        }

        BranchDeliveryMethodDTO branchDeliveryMethodDTO = (BranchDeliveryMethodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, branchDeliveryMethodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchDeliveryMethodDTO{" +
            "id=" + getId() +
            ", deliveryMetod='" + getDeliveryMetod() + "'" +
            ", branch=" + getBranch() +
            "}";
    }
}
