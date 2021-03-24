package org.elliotnash.teilochat.core;

import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;
import org.elliotnash.teilochat.core.config.TeiloChatConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {

        ConfigManager manager = new ConfigManager("/Users/elliot/Downloads/config.yml");

        HashMap<String, PlayerFormat> test = new HashMap<>();

        test.put("test", new PlayerFormat());

        System.out.println(test.get("test"));

        PlayerFormat tmp = test.get("test");
        tmp.msgprefix = "fuck";

        System.out.println(test);

    }
}
