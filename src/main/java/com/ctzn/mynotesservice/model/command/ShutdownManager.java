package com.ctzn.mynotesservice.model.command;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ShutdownManager {

    private ApplicationContext appContext;

    public ShutdownManager(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Async
    public void initiateShutdownAsync(int returnCode) {
        try {
            // sleep while the NIO thread responds with a status code and closes the connection
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            SpringApplication.exit(appContext, () -> returnCode);
        }
    }

}
