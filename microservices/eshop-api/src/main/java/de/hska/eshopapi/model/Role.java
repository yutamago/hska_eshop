package de.hska.eshopapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Role {
    @JsonProperty private UUID roleId;
    @JsonProperty private String type;
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
