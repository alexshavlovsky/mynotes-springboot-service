package com.ctzn.mynotesservice.configuration;

import com.ctzn.mynotesservice.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.api.url.users}")
    private String apiUrlUsers;

    @Value("${app.api.url.users.login}")
    private String apiUrlUsersLogin;

    @Value("${app.api.url.users.current}")
    private String apiUrlUsersCurrent;

    @Value("${app.api.url.command}")
    private String apiUrlCommand;

    @Value("${app.api.url.feedback}")
    private String apiUrlFeedback;

    @Value("${app.api.url.notes}")
    private String apiUrlNotes;

    @Value("${app.api.url.notebooks}")
    private String apiUrlNotebooks;


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
                // H2 console for debug purposes only
                // TODO: disable it in prod
                .antMatchers("/h2-console/**").permitAll()
                // public swagger endpoints
                .antMatchers("/swagger-resources/**", "/webjars/**", "/v2/api-docs").permitAll()
                // public frontend app
                .antMatchers("/*").permitAll()
                // admin endpoints
                .antMatchers(HttpMethod.GET, apiUrlUsers).hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, apiUrlUsers + "/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, apiUrlUsers + "/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, apiUrlCommand).hasRole("ADMIN")
                // public authentication api
                .antMatchers(HttpMethod.POST, apiUrlUsers).permitAll()
                .antMatchers(HttpMethod.POST, apiUrlUsersLogin).permitAll()
                // resolve user by token
                .antMatchers(HttpMethod.GET, apiUrlUsersCurrent).authenticated()
                // user endpoints
                .antMatchers(apiUrlNotes + "/**").hasRole("USER")
                .antMatchers(apiUrlNotebooks + "/**").hasRole("USER")
                .antMatchers(apiUrlFeedback + "/*").hasRole("USER")
                .anyRequest().denyAll();
    }

}
