package de.hska.eshopapi.core.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.Type;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Role.findByType", query = "select r from Role r where r.type = :type and r.isDeleted = false"),
})
public class Role {
    @Id
    @Column(nullable = false)
    @JsonProperty
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID roleId = UUID.randomUUID();

    @Column(nullable = false)
    @JsonProperty
    private String type;

    @Column(nullable = false)
    @JsonProperty
    private int level;

    @Column(nullable = false)
    @JsonProperty
    private boolean isDeleted;

    public Role(){}

    public Role(UUID roleId, String type, int level, boolean isDeleted) {
        this.roleId = roleId;
        this.type = type;
        this.level = level;
        this.isDeleted = isDeleted;
    }

    public Role(String type, int level, boolean isDeleted) {
        this.type = type;
        this.level = level;
        this.isDeleted = isDeleted;
    }

    public Role(Role role) {
        this.roleId = role.roleId == null ? this.roleId : role.roleId;
        this.isDeleted = role.isDeleted;
        this.level = role.level;
        this.type = role.type;
    }

    @Transactional
    public Role makeNew() {
        Role newRole = new Role();
        newRole.roleId = this.roleId == null ? newRole.roleId : this.roleId;
        newRole.isDeleted = this.isDeleted;
        newRole.level = this.level;
        newRole.type = this.type;
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
}
