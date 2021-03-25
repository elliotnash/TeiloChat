package org.elliotnash.teilochat.paper;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.elliotnash.teilochat.core.config.ConfigManager;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public final class TeiloChat extends JavaPlugin {

    public static Logger logger;
    public static Plugin plugin;

    public static Plugin getMain(){
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = this.getLogger();
        plugin = this;

        //get config
        ConfigManager config = new ConfigManager(this.getDataFolder()+"/config.yml");
        Optional<String> result = config.read();
        if (result.isPresent()){
            logger.warning("Error reading configuration file!");
            logger.warning(result.get());
        }

        getServer().getPluginManager().registerEvents(new ChatListener(config), this);

        getCommand("teilochat").setExecutor(new CommandListener(config));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
