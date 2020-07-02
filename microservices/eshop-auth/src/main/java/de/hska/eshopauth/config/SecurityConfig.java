package de.hska.eshopauth.config;

import de.hska.eshopauth.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService authUserDetailService;

    @Autowired
    public SecurityConfig(UserDetailsService authUserDetailService) {
        this.authUserDetailService = authUserDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {

//        PasswordEncoder encoder = this.passwordEncoder();

        // final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        return authUserDetailService;
    }

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll();
//                .mvcMatchers("/oauth2/keys", "/oauth/token", "/auth/register").permitAll()
//                .mvcMatchers(HttpMethod.POST, "/auth/register").permitAll()

                //                .antMatchers("/**").permitAll();
                //                .antMatchers("/oauth2/keys", "/token").permitAll()
//                .anyRequest().authenticated();
//                .and()
//                .formLogin();
    }
    // @formatter:on

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new DelegatingPasswordEncoder("custom", new HashMap<>(Map.of(
                "noop", new CustomPasswordEncoder(),
                "custom", new CustomPasswordEncoder(),
                "sha256", new CustomPasswordEncoder()
        )));
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
