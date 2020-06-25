package de.hska.eshopapi.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.ws.rs.HttpMethod;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "oauth2";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer security) throws Exception {
        security
                .resourceId(RESOURCE_ID)
                .tokenStore(this.tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
//                    .mvcMatchers("/user/**", "/role/**").authenticated()
                .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/login").failureUrl("/login-error").permitAll();

//                .and()
//                .authorizeRequests()
//                .anyRequest().permitAll();
//                .and()
//                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());


//                .anyRequest().permitAll();
//                .antMatchers("/login").permitAll()
//
////                .antMatchers("/dev/**").access("#oauth2.hasScope('dev')")
//
//                .antMatchers(HttpMethod.GET, "/user/**").access("#oauth2.hasScope('user.read')")
//                .antMatchers( "/user/**").access("#oauth2.hasScope('user.write')")
//
//                .antMatchers(HttpMethod.GET, "/role/**").access("#oauth2.hasScope('role.read')")
//                .antMatchers("/role/**").access("#oauth2.hasScope('role.write')")
//
//                .antMatchers("/**").access("#oauth2.hasScope('dev')")
//
//                .anyRequest().denyAll()
//
//                .and()
//                .formLogin();
        // @formatter:on
    }
}