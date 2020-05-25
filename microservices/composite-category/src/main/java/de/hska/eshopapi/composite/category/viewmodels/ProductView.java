package de.hska.eshopapi.composite.category.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.composite.category.model.Category;
import de.hska.eshopapi.composite.category.model.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductView {
    @JsonProperty private UUID productId;
    @JsonProperty private Category category;
    @JsonProperty private String name;
    @JsonProperty private BigDecimal price;
    @JsonProperty private String details;

    public static ProductView FromProduct(Product product, Category category) {
        ProductView pv = new ProductView();
        pv.category = category;
        pv.details = product.getDetails();
        pv.name = product.getName();
        pv.price = product.getPrice();
        pv.productId = product.getProductId();

        return pv;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
