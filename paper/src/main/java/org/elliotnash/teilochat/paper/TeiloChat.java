package org.elliotnash.teilochat.paper;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class TeiloChat extends JavaPlugin {

    public static FileConfiguration config;
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
        config = this.getConfig();
        logger = this.getLogger();
        plugin = this;

        if (config.isConfigurationSection("playerFormats")){
            ConfigurationSection playerFormatSect = config.getConfigurationSection("playerFormats");
            for (String key : playerFormatSect.getKeys(false)){
                UUID playerUUID;
                try{
                    playerUUID = UUID.fromString(key);
                    //do something lol
                } catch (IllegalArgumentException exception){
                    //not valid uuid, remove section then continue
                    logger.info("invalid uuid "+key+", deleting entry");
                    playerFormatSect.set(key, null);
                    continue;
                }
                if (playerFormatSect.isConfigurationSection(key)){
                    ConfigurationSection playerSect = playerFormatSect.getConfigurationSection(key);
                    formatMap.put(playerUUID, (HashMap) playerSect.getValues(false));
                }
            }
        } else{
            this.getConfig().createSection("playerFormats", formatMap);
        }

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        getCommand("teilochat").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
