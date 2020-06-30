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

@Entity
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "select u from User u where u.username = :username and u.isDeleted = false"),
        // TODO: password -> SHA HASH
        @NamedQuery(name = "User.findByUsernamePassword", query = "select u from User u where u.username = :username and u.password = :password and u.isDeleted = false")
})
public class User implements UserDetails {

    public static User makeNew(User user) {
        User newUser = new User();
        newUser.firstname = user.firstname;
        newUser.isDeleted = user.isDeleted;
        newUser.lastname = user.lastname;
        newUser.password = user.password;
        newUser.roleId = user.roleId;
        newUser.username = user.username;
        return newUser;
    }


    @Id
    @Column(nullable = false)
    @JsonProperty
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID userId = UUID.randomUUID();

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
    @Type(type="org.hibernate.type.UUIDCharType")
    @JsonProperty private UUID roleId;

    @Column(nullable = false)
    @JsonProperty private boolean isDeleted;

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
        return !this.isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isDeleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isDeleted;
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
