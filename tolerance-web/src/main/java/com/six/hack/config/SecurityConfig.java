package com.six.hack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication = auth
                .inMemoryAuthentication();
        inMemoryAuthentication.withUser("Admin").password("test").roles("USER");
        inMemoryAuthentication.withUser("Keith").password("test").roles("USER");
        inMemoryAuthentication.withUser("Jun").password("test").roles("USER");
        inMemoryAuthentication.withUser("Manuel").password("test").roles("USER");
        inMemoryAuthentication.withUser("Patrick").password("test").roles("USER");
    }
}
