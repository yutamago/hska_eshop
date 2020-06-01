package de.hska.eshopapi.core.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Category.findByName", query = "select c from Category c where c.name = :name and c.isDeleted = false"),
        @NamedQuery(name = "Category.findByProductId", query = "select c from Category c where :productId in(c.productIds) and c.isDeleted = false"),
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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> productIds;

    @Column(nullable = false)
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
