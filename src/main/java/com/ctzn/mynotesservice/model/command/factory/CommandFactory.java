package com.ctzn.mynotesservice.model.command.factory;

import com.ctzn.mynotesservice.model.command.ShutdownManager;
import com.ctzn.mynotesservice.repositories.DbSeeder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandFactory {
    private DbSeeder dbSeeder;
    private ShutdownManager shutdownManager;

    public CommandFactory(DbSeeder dbSeeder, ShutdownManager shutdownManager) {
        this.dbSeeder = dbSeeder;
        this.shutdownManager = shutdownManager;
    }

    private final Map<String, Command> commandsMap = createCommandsMap();

    private Map<String, Command> createCommandsMap() {
        HashMap<String, Command> map = new HashMap<>();
        map.put("shutdown", new Command("Shutdown command accepted", () -> shutdownManager.initiateShutdown(0)));
        map.put("fill database", new Command("Fill database command accepted", () -> dbSeeder.run("force")));
        return map;
    }

    public boolean commandWithKeyWordExists(String keyWord) {
        return commandsMap.containsKey(keyWord);
    }

    public Command getCommandByKeyWord(String keyWord) {
        return commandsMap.get(keyWord);
    }

}
