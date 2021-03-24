package org.elliotnash.teilochat.core;

import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {

        ConfigManager manager = new ConfigManager("/Users/elliot/Desktop/config.yml");

        manager.add("548612a6-c689-4aef-995b-fd00eb6ae664", new PlayerFormat());

        manager.write();

    }
}
