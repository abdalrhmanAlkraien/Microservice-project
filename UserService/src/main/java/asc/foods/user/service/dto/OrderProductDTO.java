package asc.foods.user.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.user.domain.OrderProduct} entity.
 */
public class OrderProductDTO implements Serializable {

    private Long id;

    private Integer quantity;

    private String optionsDesc;

    private String specialRequest;

    private Double price;

    private Double discount;

    private Instant creationDate;

    private AscOrderDTO order;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOptionsDesc() {
        return optionsDesc;
    }

    public void setOptionsDesc(String optionsDesc) {
        this.optionsDesc = optionsDesc;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public AscOrderDTO getOrder() {
        return order;
    }

    public void setOrder(AscOrderDTO order) {
        this.order = order;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderProductDTO)) {
            return false;
        }

        OrderProductDTO orderProductDTO = (OrderProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderProductDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", optionsDesc='" + getOptionsDesc() + "'" +
            ", specialRequest='" + getSpecialRequest() + "'" +
            ", price=" + getPrice() +
            ", discount=" + getDiscount() +
            ", creationDate='" + getCreationDate() + "'" +
            ", order=" + getOrder() +
            ", product=" + getProduct() +
            "}";
    }
}
