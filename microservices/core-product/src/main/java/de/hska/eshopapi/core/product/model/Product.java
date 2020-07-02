package de.hska.eshopapi.core.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Entity
public class Product {
    public static Product makeNew(Product product) {
        Product newProduct = new Product();
        newProduct.categoryId = product.categoryId;
        newProduct.details = product.details;
        newProduct.isDeleted = product.isDeleted;
        newProduct.name = product.name;
        newProduct.price = product.price;
        return newProduct;
    }

    @Id
    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    @JsonProperty private UUID productId = UUID.randomUUID();

    @Column(nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
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
