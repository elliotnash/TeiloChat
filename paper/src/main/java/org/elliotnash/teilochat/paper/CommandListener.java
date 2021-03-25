package org.elliotnash.teilochat.paper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

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

        LinkedList<String> commands = new LinkedList<String>();
        final LinkedList<String> completions = new LinkedList<String>();
        switch (args.length){
            case 1:
                commands = new LinkedList<String>(Arrays.asList("name", "msgprefix", "reset"));
                StringUtil.copyPartialMatches(args[0], commands, completions);
                break;
            case 2:
                if (sender.hasPermission("teilochat.other")){
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
                        commands.add(player.getName());
                    }
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                }
                break;
        }

        Collections.sort(completions);
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        handler.command(new PaperSender(sender), concat(args));

        return true;
    }

}
