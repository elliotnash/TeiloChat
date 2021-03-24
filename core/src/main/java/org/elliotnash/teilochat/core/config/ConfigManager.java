package org.elliotnash.teilochat.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private final TeiloChatConfig config;
    private ObjectMapper mapper;
    private final File configFile;

    public ConfigManager(String configPath) throws IOException {
        configFile = new File(configPath);

        mapper = new ObjectMapper(new YAMLFactory());
        config = mapper.readValue(configFile, TeiloChatConfig.class);
    }

    public PlayerFormat get(String uuid){return get(UUID.fromString(uuid));}
    public PlayerFormat get(UUID uuid){
        return config.playerFormats.get(uuid);
    }

    public void add(String uuid, PlayerFormat playerFormat){add(UUID.fromString(uuid), playerFormat);}
    public void add(UUID uuid, PlayerFormat playerFormat){
        config.playerFormats.putIfAbsent(uuid, playerFormat);
    }

    //Write must be explicitly called after every modification
    public void write(){
        try {
            mapper.writeValue(new File("/Users/elliot/Desktop/config2.yml"), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return config.playerFormats.toString();
    }

}
