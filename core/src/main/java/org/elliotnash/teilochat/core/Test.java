package org.elliotnash.teilochat.core;

import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

import java.io.*;
import java.util.InputMismatchException;

public class Test {
    public static void main(String[] args) {

        ConfigManager manager;
        try {
            manager = new ConfigManager("/Users/elliot/Desktop/config.yml");
        } catch (InputMismatchException e){
            System.out.println("Invalid configuration file, disabling");
            System.out.println(e.getMessage());;
            return;
        }

    }
}
