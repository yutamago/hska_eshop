package de.hska.eshopapi.composite.category.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.composite.category.model.Category;
import de.hska.eshopapi.composite.category.model.Product;

import java.util.List;
import java.util.UUID;

public class CategoryView {
    @JsonProperty private UUID categoryId;
    @JsonProperty private String name;
    @JsonProperty private List<Product> products;

    public static CategoryView FromCategory(Category c, List<Product> productsCategory) {
        CategoryView cv = new CategoryView();
        cv.categoryId = c.getCategoryId();
        cv.name = c.getName();
        cv.products = productsCategory;
        return cv;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
