package com.ctzn.mynotesservice.model.command.context;

import com.ctzn.mynotesservice.model.command.ShutdownManager;
import com.ctzn.mynotesservice.repositories.DbSeeder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExecutionContext {
    private DbSeeder dbSeeder;
    private ShutdownManager shutdownManager;

    public ExecutionContext(DbSeeder dbSeeder, ShutdownManager shutdownManager) {
        this.dbSeeder = dbSeeder;
        this.shutdownManager = shutdownManager;
    }

    private final Map<String, Command> commandsMap = createCommandsMap();

    private Map<String, Command> createCommandsMap() {
        HashMap<String, Command> map = new HashMap<>();
        map.put("shutdown", new Command("Shutdown command accepted", () -> shutdownManager.initiateShutdownAsync(0)));
        map.put("fill database", new Command("Fill database command accepted", () -> dbSeeder.seedAsync()));
        return map;
    }

    public boolean commandWithKeyWordExists(String keyWord) {
        return commandsMap.containsKey(keyWord);
    }

    public String executeCommandByKeyWord(String keyWord) {
        Command command = commandsMap.get(keyWord);
        command.apply();
        return command.getDebugMessage();
    }


}
