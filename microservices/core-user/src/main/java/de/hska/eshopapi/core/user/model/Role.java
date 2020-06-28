package de.hska.eshopapi.core.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Role.findByType", query = "select r from Role r where r.type = :type and r.isDeleted = false"),
})
@Eager
public class Role {
    @Id
    @Column(nullable = false)
    @JsonProperty private UUID roleId = UUID.randomUUID();

    @Column(nullable = false)
    @JsonProperty private String type;

    @Column(nullable = false)
    @JsonProperty private int level;

    @Column(nullable = false)
    @JsonProperty private boolean isDeleted;

    public static Role makeNew(Role role) {
        Role newRole = new Role();
        newRole.roleId = role.roleId == null ? newRole.roleId : role.roleId;
        newRole.isDeleted = role.isDeleted;
        newRole.level = role.level;
        newRole.type = role.type;
        return newRole;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
