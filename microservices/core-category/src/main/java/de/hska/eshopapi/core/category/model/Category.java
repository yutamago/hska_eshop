package de.hska.eshopapi.core.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Category.findByName", query = "select c from Category c where c.name = :name"),
        @NamedQuery(name = "Category.findByProductId", query = "select c from Category c where :productId in(c.productIds)"),
})
public class Category {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @JsonProperty private UUID categoryId;

    @Column(nullable = false)
    @JsonProperty private String name;

    @Column(nullable = false)
    @JsonProperty
    @ElementCollection
    private Set<UUID> productIds;

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
}
