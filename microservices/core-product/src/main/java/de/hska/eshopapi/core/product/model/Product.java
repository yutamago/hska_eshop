package de.hska.eshopapi.core.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Entity
@NamedQueries({
        @NamedQuery(name = "findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
        @NamedQuery(name = "findByCategoryId", query = "SELECT p FROM Product p WHERE p.categoryId = :categoryId"),
})
public class Product {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @JsonProperty private UUID productId;

    @Column(nullable = false)
    @JsonProperty private UUID categoryId;

    @Column(nullable = false)
    @JsonProperty private String name;

    @Column(nullable = false)
    @JsonProperty private BigDecimal price;

    @Column(nullable = false)
    @JsonProperty private String details;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
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
