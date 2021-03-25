package org.elliotnash.teilochat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Optional;

@Plugin(
        id = "teilochat",
        name = "TeiloChat-Velocity",
        version = "2.0-SNAPSHOT",
        description = "Add chat customization",
        url = "https://github.com/elliotnash/TeiloChat",
        authors = {"Elliot Nash"}
)
public class TeiloChat {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public TeiloChat(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory){
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        ConfigManager config = new ConfigManager(dataDirectory.resolve("config.yml"));
        Optional<String> result = config.read();
        if (result.isPresent()){
            logger.error("Error reading configuration file!");
            logger.error(result.get());
        }

        server.getEventManager().register(this, new ChatListener(server, config));

        server.getCommandManager().register("tc", new CommandListener(server, config));

    }

    public void onChat(){

    }
}
