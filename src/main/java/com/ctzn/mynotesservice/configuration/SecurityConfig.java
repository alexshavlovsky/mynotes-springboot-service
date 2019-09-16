package com.ctzn.mynotesservice.configuration;

import com.ctzn.mynotesservice.model.command.CommandController;
import com.ctzn.mynotesservice.model.user.UserController;
import com.ctzn.mynotesservice.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // PREVENT SPRING SECURITY AUTO-CONFIGURATION
        return super.authenticationManagerBean();
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
                // TODO: check if h2-console and swagger are disabled in prod
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-resources/**", "/webjars/**", "/v2/api-docs").permitAll()
                .antMatchers("/*").permitAll() // frontend app
                .antMatchers(HttpMethod.POST, UserController.BASE_PATH).permitAll() // register
                .antMatchers(HttpMethod.POST, UserController.LOGIN_PATH).permitAll() // login
                .antMatchers(HttpMethod.POST, CommandController.BASE_PATH).hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, UserController.BASE_PATH).hasRole("ADMIN")
                .anyRequest().authenticated();
    }

}



