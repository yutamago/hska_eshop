package de.hska.eshopapi.core.product.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.core.product.model.Product;

import java.io.Serializable;
import java.util.UUID;

public class ProductView implements Serializable {
    @JsonProperty private UUID roleId;
    @JsonProperty private String type;
    @JsonProperty private int level;

    public static ProductView FromRole(Product product) {
        ProductView rv = new ProductView();
        rv.level = product.getLevel();
        rv.roleId = product.getRoleId();
        rv.type = product.getType();
        return rv;
    }

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
