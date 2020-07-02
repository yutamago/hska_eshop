package de.hska.eshopauth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class OAuthUser implements UserDetails {

    public static OAuthUser convert(User user) {
        OAuthUser newUser = new OAuthUser();
        newUser.userId = user.getUserId();
        newUser.firstname = user.getFirstname();
        newUser.lastname = user.getLastname();
        newUser.password = "{noop}" + user.getPassword();
        newUser.roleId = user.getRoleId();
        newUser.username = user.getUsername();
        return newUser;
    }


    private UUID userId;
    private String username;
    private String firstname;
    private String lastname;

    // HASH!!!
    private String password;

    private UUID roleId;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuth = new ArrayList<>();
        if ("admin".equals(username)) {
            grantedAuth.addAll(List.of(
                    new SimpleGrantedAuthority("USER"),
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("dev"),
                    new SimpleGrantedAuthority("user.read"),
                    new SimpleGrantedAuthority("user.write"),
                    new SimpleGrantedAuthority("role.read"),
                    new SimpleGrantedAuthority("role.write"),
                    new SimpleGrantedAuthority("category.read"),
                    new SimpleGrantedAuthority("category.write"),
                    new SimpleGrantedAuthority("product.read"),
                    new SimpleGrantedAuthority("product.write")
            ));
        } else {
            grantedAuth.addAll(List.of(
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("USER"),
                    new SimpleGrantedAuthority("user.read"),
                    new SimpleGrantedAuthority("role.read"),
                    new SimpleGrantedAuthority("category.read"),
                    new SimpleGrantedAuthority("product.read")
            ));
        }

        return grantedAuth;
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
