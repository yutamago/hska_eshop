package de.hska.eshopapi.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "oauth2";

//    private TokenStore tokenStore;
//
//    @Autowired
//    public ResourceServerConfig(TokenStore tokenStore) {
//        this.tokenStore = tokenStore;
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer security) throws Exception {
//        security
//                .resourceId(RESOURCE_ID)
//                .tokenStore(this.tokenStore);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .antMatcher("/role/**").authorizeRequests()
                .antMatchers("/role/**").access("#oauth2.hasScope('role.read')");
        // @formatter:on
    }
}