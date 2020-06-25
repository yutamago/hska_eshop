package hska.iwi.eShopMaster.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import hska.iwi.eShopMaster.model.Product;

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
