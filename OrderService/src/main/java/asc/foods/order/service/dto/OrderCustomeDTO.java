package asc.foods.order.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.OrderCustome} entity.
 */
public class OrderCustomeDTO implements Serializable {

    private Long id;

    private ProductDTO product;

    private AscOrderDTO ascOrder;

    private MealCustmizeDTO mealCustmize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public AscOrderDTO getAscOrder() {
        return ascOrder;
    }

    public void setAscOrder(AscOrderDTO ascOrder) {
        this.ascOrder = ascOrder;
    }

    public MealCustmizeDTO getMealCustmize() {
        return mealCustmize;
    }

    public void setMealCustmize(MealCustmizeDTO mealCustmize) {
        this.mealCustmize = mealCustmize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderCustomeDTO)) {
            return false;
        }

        OrderCustomeDTO orderCustomeDTO = (OrderCustomeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderCustomeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCustomeDTO{" +
            "id=" + getId() +
            ", product=" + getProduct() +
            ", ascOrder=" + getAscOrder() +
            ", mealCustmize=" + getMealCustmize() +
            "}";
    }
}
