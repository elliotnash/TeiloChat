package org.elliotnash.teilochat.core;

import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {

        ConfigManager manager;
        try {
            manager = new ConfigManager("/Users/elliot/Desktop/config.yml");
        } catch (IOException e) {
            return;
        }

        manager.write();

    }
}
