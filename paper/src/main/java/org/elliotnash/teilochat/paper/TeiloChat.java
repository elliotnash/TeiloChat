package org.elliotnash.teilochat.paper;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.elliotnash.teilochat.core.config.ConfigManager;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class TeiloChat extends JavaPlugin {

    public static ConfigManager config;
    public static HashMap<UUID, HashMap<String, String>> formatMap = new HashMap<>();
    public static Logger logger;
    public static Plugin plugin;

    public static Plugin getMain(){
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        //get config
        config = new ConfigManager(this.getDataFolder()+"/config.yml");
        logger = this.getLogger();
        plugin = this;


        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        getCommand("teilochat").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
