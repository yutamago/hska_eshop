package de.hska.eshopauth.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopauth.model.Role;

import java.io.Serializable;
import java.util.UUID;

public class RoleView implements Serializable {
    @JsonProperty
    private UUID roleId;
    @JsonProperty
    private String type;
    @JsonProperty
    private int level;

    public static RoleView FromRole(Role role) {
        RoleView rv = new RoleView();
        rv.level = role.getLevel();
        rv.roleId = role.getRoleId();
        rv.type = role.getType();
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
