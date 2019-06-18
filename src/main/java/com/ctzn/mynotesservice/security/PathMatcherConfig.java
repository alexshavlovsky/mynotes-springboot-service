package com.ctzn.mynotesservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathMatcherConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // exact path matching is important when setting up
        // path-based access rules in the WebSecurityConfigurerAdapter
        configurer.setUseTrailingSlashMatch(false);
    }

}
