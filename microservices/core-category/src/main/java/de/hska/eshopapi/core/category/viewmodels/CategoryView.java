package de.hska.eshopapi.core.category.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.core.category.model.Category;

import java.io.Serializable;
import java.util.UUID;

public class CategoryView implements Serializable {
    @JsonProperty private UUID roleId;
    @JsonProperty private String type;
    @JsonProperty private int level;

    public static CategoryView FromRole(Category category) {
        CategoryView rv = new CategoryView();
        rv.level = category.getLevel();
        rv.roleId = category.getRoleId();
        rv.type = category.getType();
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
