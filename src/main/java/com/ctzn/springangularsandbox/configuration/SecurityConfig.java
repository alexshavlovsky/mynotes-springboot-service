package com.ctzn.springangularsandbox.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// Enable the tomcat basic authentication
@Configuration
@ConditionalOnProperty(name = "app.auth.enabled", havingValue = "true")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.auth.username}")
    private String userName;

    @Value("${app.auth.password}")
    private String userPassword;

    @Value("${app.auth.realm}")
    private String realmName;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
        http.httpBasic().realmName(realmName);
        http.csrf().disable();
        //http.headers().frameOptions().disable(); // to enable h2-console traffic
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        String encodedPassword = passwordEncoder().encode(userPassword);
        manager.createUser(User.withUsername(userName).password(encodedPassword).roles("USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}



