package de.hska.eshopapi.core.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Role.findByType", query = "select r from Product r where r.type = :type"),
})
public class Product {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @JsonProperty private UUID roleId;

    @Column(nullable = false)
    @JsonProperty private String type;

    @Column(nullable = false)
    @JsonProperty private int level;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
