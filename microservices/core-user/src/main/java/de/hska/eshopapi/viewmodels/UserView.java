package de.hska.eshopapi.viewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hska.eshopapi.model.Role;
import de.hska.eshopapi.model.User;

import java.io.Serializable;
import java.util.UUID;

public class UserView implements Serializable {
    @JsonProperty private UUID userId;
    @JsonProperty private String username;
    @JsonProperty private String firstname;
    @JsonProperty private String lastname;
    @JsonProperty private String password;
    @JsonProperty private RoleView role;

    public static UserView FromUser(User user, Role role) {
        UserView uv = new UserView();
        uv.userId = user.getUserId();
        uv.username = user.getUsername();
        uv.firstname = user.getFirstname();
        uv.lastname = user.getLastname();
        uv.password = user.getPassword();
        uv.role = RoleView.FromRole(role);
        return uv;
    }

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

    public RoleView getRole() {
        return role;
    }

    public void setRole(RoleView role) {
        this.role = role;
    }
}
