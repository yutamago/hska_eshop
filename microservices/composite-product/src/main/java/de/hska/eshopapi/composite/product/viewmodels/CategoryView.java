package de.hska.eshopapi.composite.product.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.composite.product.model.Product;

import java.util.List;
import java.util.UUID;

public class CategoryView {
    @JsonProperty private UUID categoryId;
    @JsonProperty private String name;
    @JsonProperty private List<Product> products;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
