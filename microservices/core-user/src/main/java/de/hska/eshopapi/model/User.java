package de.hska.eshopapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "select u from User u where u.username = :username"),
        // TODO: password -> SHA HASH
        @NamedQuery(name = "User.findByUsernamePassword", query = "select u from User u where u.username = :username and u.password = :password")
})
public class User {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @JsonProperty private UUID userId;

    @Column(nullable = false)
    @JsonProperty private String username;

    @Column(nullable = false)
    @JsonProperty private String firstname;

    @Column(nullable = false)
    @JsonProperty private String lastname;

    // HASH!!!
    @Column(nullable = false)
    @JsonProperty private String password;

    @Column(nullable = false)
    @JsonProperty private UUID roleId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    private String makeHash(String something) {
        return something;
    }
}
