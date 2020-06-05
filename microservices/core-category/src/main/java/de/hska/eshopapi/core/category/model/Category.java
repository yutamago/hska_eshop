package de.hska.eshopapi.core.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
public class Category {

    public static Category makeNew(Category category) {
        Category newCategory = new Category();
        newCategory.productIds = category.productIds == null ? new ArrayList<>() : new ArrayList<>(category.productIds);
        newCategory.isDeleted = category.isDeleted;
        newCategory.name = category.name;
        return newCategory;
    }

    @Id
    @Column(nullable = false)
    @JsonProperty private UUID categoryId = UUID.randomUUID();

    @Column(nullable = false)
    @JsonProperty private String name;

    @Column(nullable = false)
    @JsonProperty
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> productIds = new ArrayList<>();

    @Column(nullable = false)
    @JsonProperty private boolean isDeleted = false;

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

    public List<UUID> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<UUID> productIds) {
        this.productIds = productIds;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
