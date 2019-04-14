package com.ctzn.springangularsandbox.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggingConfiguration {
    @Bean
    @Scope("prototype")
    public Logger logger(final InjectionPoint ip) {
        return LoggerFactory.getLogger(
                (null != ip.getMethodParameter()) ?
                        ip.getMethodParameter().getContainingClass() :
                        ip.getField().getDeclaringClass()
        );
    }
}
