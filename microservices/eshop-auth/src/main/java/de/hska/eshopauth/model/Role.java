package de.hska.eshopauth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Role.findByType", query = "select r from Role r where r.type = :type"),
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

    public Role(){}

    public Role(UUID roleId, String type, int level) {
        this.roleId = roleId;
        this.type = type;
        this.level = level;
    }

    public Role(String type, int level) {
        this.type = type;
        this.level = level;
    }

    public Role(Role role) {
        this.roleId = role.roleId == null ? this.roleId : role.roleId;
        this.level = role.level;
        this.type = role.type;
    }

    @Transactional
    public Role makeNew() {
        Role newRole = new Role();
        newRole.roleId = this.roleId == null ? newRole.roleId : this.roleId;
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
