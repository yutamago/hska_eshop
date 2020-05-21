package de.hska.eshopapi.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.model.Role;

import java.util.UUID;

public class UserView {
    @JsonProperty private UUID userId;
    @JsonProperty private String username;
    @JsonProperty private String firstname;
    @JsonProperty private String lastname;
    @JsonProperty private String password;
    @JsonProperty private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
