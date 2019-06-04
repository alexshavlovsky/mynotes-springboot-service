package com.ctzn.mynotesservice.security;

import com.ctzn.mynotesservice.model.login.LoginController;
import com.ctzn.mynotesservice.model.user.UserController;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and().headers().frameOptions().sameOrigin()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/h2-console/*").permitAll()
                .antMatchers(HttpMethod.POST, UserController.BASE_PATH).permitAll()
                .antMatchers(HttpMethod.POST, LoginController.BASE_PATH).permitAll()
                .antMatchers("/api/notebooks/*").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

}



