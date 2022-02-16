package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.ProductBranch} entity.
 */
public class ProductBranchDTO implements Serializable {

    private Long id;

    private ProductStatus productStatus;

    private ProductDTO product;

    private BranchDTO branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
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
        if (!(o instanceof ProductBranchDTO)) {
            return false;
        }

        ProductBranchDTO productBranchDTO = (ProductBranchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productBranchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductBranchDTO{" +
            "id=" + getId() +
            ", productStatus='" + getProductStatus() + "'" +
            ", product=" + getProduct() +
            ", branch=" + getBranch() +
            "}";
    }
}
