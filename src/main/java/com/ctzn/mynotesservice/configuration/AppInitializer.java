package com.ctzn.mynotesservice.configuration;

import com.ctzn.mynotesservice.repositories.DbSeeder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppInitializer implements CommandLineRunner {

    private DbSeeder dbSeeder;

    public AppInitializer(DbSeeder dbSeeder) {
        this.dbSeeder = dbSeeder;
    }

    @Override
    public void run(String... args) {
        log.debug("Inside app initializer...");
        dbSeeder.seed(false);
    }

}
