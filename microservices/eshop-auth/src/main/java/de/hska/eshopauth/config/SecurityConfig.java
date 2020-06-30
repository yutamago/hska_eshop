package de.hska.eshopauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
// TODO: @EnableOAuth2Sso deprecated? brauchen wir das?
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/oauth2/keys", "/oauth/token").permitAll()
//                .antMatchers("/**").permitAll();
//                .antMatchers("/oauth2/keys", "/token").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }
    // @formatter:on
//
//    @Bean
//    public UserDetailsService users() throws Exception {
//        @SuppressWarnings("deprecation")
//        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        manager.createUser(users
//                .username("oauthuser")
//                .password("oauthpassword")
//                .roles("USER",
//                        "user.read",
//                        "role.read",
//                        "category.read",
//                        "product.read")
//                .build());
//        manager.createUser(users
//                .username("admin")
//                .password("password")
//                .roles("USER", "ADMIN", "dev",
//                        "user.read", "user.write",
//                        "role.read", "role.write",
//                        "category.read", "category.write",
//                        "product.read", "product.write"
//                )
//                .build());
//
//        return manager;
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
