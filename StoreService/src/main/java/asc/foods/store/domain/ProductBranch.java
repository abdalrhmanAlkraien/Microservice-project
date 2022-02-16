package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductBranch.
 */
@Entity
@Table(name = "product_branch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "productOptions", "orders", "userProducts", "itemTypes", "orderCustomes", "productBranches", "store", "foodGenre", "productTag",
        },
        allowSetters = true
    )
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "productBranches", "branchDeliveryMethods", "store" }, allowSetters = true)
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductBranch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductStatus getProductStatus() {
        return this.productStatus;
    }

    public ProductBranch productStatus(ProductStatus productStatus) {
        this.setProductStatus(productStatus);
        return this;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductBranch product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public ProductBranch branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductBranch)) {
            return false;
        }
        return id != null && id.equals(((ProductBranch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductBranch{" +
            "id=" + getId() +
            ", productStatus='" + getProductStatus() + "'" +
            "}";
    }
}
