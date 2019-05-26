package com.ctzn.mynotesservice.components;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ShutdownManager {

    private ApplicationContext appContext;

    public ShutdownManager(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    public void initiateShutdown(int returnCode) {
        SpringApplication.exit(appContext, () -> returnCode);
    }

}
