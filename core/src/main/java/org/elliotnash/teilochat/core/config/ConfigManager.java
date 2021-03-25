package org.elliotnash.teilochat.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.UUID;

public class ConfigManager {

    private TeiloChatConfig config;
    private final ObjectMapper mapper;
    private final File configFile;

    public ConfigManager(String configPath) throws InputMismatchException {
        this(new File(configPath));
    }
    public ConfigManager(Path configPath) throws InputMismatchException {
        this(configPath.toFile());
    }
    public ConfigManager(File configFile) throws InputMismatchException {
        this.configFile = configFile;
        mapper = new ObjectMapper(new YAMLFactory());
    }

    public Optional<String> read(){
        configFile.getParentFile().mkdirs();
        try {
            config = mapper.readValue(configFile, TeiloChatConfig.class);
            return Optional.empty();
        } catch (MismatchedInputException e){
            config = new TeiloChatConfig();
            return Optional.of(e.getLocalizedMessage());
        } catch (IOException e) {
            config = new TeiloChatConfig();
            return Optional.empty();
        }
    }

    public PlayerFormat get(String uuid){return get(UUID.fromString(uuid));}
    public PlayerFormat get(UUID uuid){
        return config.playerFormats.get(uuid);
    }

    public void add(String uuid, PlayerFormat playerFormat){add(UUID.fromString(uuid), playerFormat);}
    public void add(UUID uuid, PlayerFormat playerFormat){
        config.playerFormats.putIfAbsent(uuid, playerFormat);
    }

    public boolean contains(String uuid){return contains(UUID.fromString(uuid));}
    public boolean contains(UUID uuid){
        return config.playerFormats.containsKey(uuid);
    }

    public void remove(String uuid){remove(UUID.fromString(uuid));}
    public void remove(UUID uuid){
        config.playerFormats.remove(uuid);
    }

    //Write must be explicitly called after every modification
    public void write(){
        try {
            mapper.writeValue(configFile, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return config.playerFormats.toString();
    }

}
