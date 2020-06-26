package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class PasswordGrantRequestBody {
    @JsonProperty("grant_type")
    private String grant_type = "password";
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String scope = "";


    public PasswordGrantRequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
