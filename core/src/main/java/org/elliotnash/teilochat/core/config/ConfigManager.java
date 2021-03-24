package org.elliotnash.teilochat.core.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private TeiloChatConfig config;

    public ConfigManager(String configPath) throws FileNotFoundException {
        Yaml yaml = new Yaml(new Constructor(TeiloChatConfig.class));

        InputStream inputStream = new FileInputStream(new File(configPath));

        config = yaml.load(inputStream);
    }

    public PlayerFormat get(String uuid){return get(UUID.fromString(uuid));}
    public PlayerFormat get(UUID uuid){
        return config.playerFormats.get(uuid);
    }

    public void write(){

    }

}
