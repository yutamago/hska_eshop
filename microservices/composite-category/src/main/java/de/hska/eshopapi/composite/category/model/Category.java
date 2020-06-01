package de.hska.eshopapi.composite.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Category {
    @JsonProperty private UUID categoryId;
    @JsonProperty private String name;
    @JsonProperty private Set<UUID> productIds;
    @JsonProperty private boolean isDeleted;

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

    public Set<UUID> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<UUID> productIds) {
        this.productIds = productIds;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
