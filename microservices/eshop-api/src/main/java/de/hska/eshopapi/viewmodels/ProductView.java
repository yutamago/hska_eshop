package de.hska.eshopapi.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.model.Category;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductView {
    @JsonProperty private UUID productId;
    @JsonProperty private Category category;
    @JsonProperty private String name;
    @JsonProperty private BigDecimal price;
    @JsonProperty private String details;

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
