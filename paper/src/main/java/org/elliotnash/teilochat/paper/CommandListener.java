package org.elliotnash.teilochat.paper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import org.elliotnash.teilochat.core.commands.CommandHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;

public class CommandListener implements org.bukkit.command.CommandExecutor, TabCompleter {

    private final CommandHandler handler;

    public CommandListener(ConfigManager config){
        handler = new CommandHandler(config, new PaperUtils());
    }

    public String concat(String[] args){
        StringBuilder argStringBuilder = new StringBuilder();
        for (String str : args){
            argStringBuilder.append(str).append(" ");
        }
        if (args.length != 0)
            argStringBuilder.setLength(argStringBuilder.length()-1);
        return argStringBuilder.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        return handler.completion(new PaperSender(sender), concat(args));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        handler.command(new PaperSender(sender), concat(args));

        return true;
    }

}
