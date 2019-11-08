package com.ctzn.mynotesservice.configuration;

import com.ctzn.mynotesservice.repositories.DbSeeder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppInitializer implements CommandLineRunner {

    @Value("${app.api.url.base}")
    private String apiUrlBase;

    private DbSeeder dbSeeder;

    public AppInitializer(DbSeeder dbSeeder) {
        this.dbSeeder = dbSeeder;
    }

    @Override
    public void run(String... args) {
        dbSeeder.seed(false);
        log.debug("REST API is available at [{}]", apiUrlBase);
    }

}
