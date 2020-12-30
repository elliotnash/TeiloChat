package org.elliotnash.teilochat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener implements CommandExecutor, TabCompleter {

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

    public LinkedList<String> parser(String[] args){
        //convert arg array to String
        StringBuilder argStringBuilder = new StringBuilder();
        for (String str : args){
            argStringBuilder.append(str).append(" ");
        }
        argStringBuilder.setLength(argStringBuilder.length()-1);
        String argString = argStringBuilder.toString();

        //parse by spaces, but also also keep quotations
        LinkedList<String> argList = new LinkedList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(argString);
        while (m.find())
            argList.add(m.group(1).replace("\"", ""));
        return argList;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //status codes: 0 = success, 1 = improper syntax, 2 = missing permission, 3 = run from console, 4 = invalid name
        int returnCode = 0;

        LinkedList<String> argList = parser(args);

        if (argList.size() == 0) returnCode = 1;

        if (argList.size() >= 1 && argList.size() <= 3){
            switch (argList.get(0)){
                case "name":
                    returnCode = setName(sender, argList);
                    break;
                case "msgprefix":
                    returnCode = setMsgPrefix(sender, argList);
                    break;
                case "reset":
                    returnCode = reset(sender, argList);
                    break;
                default: returnCode=1; break;
            }
        }

        switch (returnCode){
            case 1:
                sender.sendMessage(ChatColor.RED+"Invalid command!");
                sender.sendMessage("/tc name <name> sets your name");
                sender.sendMessage("/tc msgprefix <prefix> sets the prefix before your message");
                sender.sendMessage("/tc reset resets your chat customization");
                sender.sendMessage("If you need to include spaces in your name or prefix, please surround it with quotes");
                sender.sendMessage("ie. /tc name \"Elliot Nash\" would set my name to Elliot Nash");
                break;
            case 2:
                sender.sendMessage(ChatColor.RED+"You are missing permission to set other people's names");
                break;
            case 3:
                sender.sendMessage(ChatColor.RED+"You can't run this message from console");
                break;
            case 4:
                sender.sendMessage(ChatColor.RED+"You can't set your name to another users' name");
        }

        return true;
    }

    public void saveConfig(){
        TeiloChat.getMain().getConfig().createSection("playerFormats", TeiloChat.formatMap);
        TeiloChat.getMain().saveConfig();
    }

    public int setName(CommandSender sender, List<String> args){
        UUID targetUUID;
        String name;
        if (args.size()==1){
            if (!(sender instanceof Player)) return 3;
            targetUUID = ((Player) sender).getUniqueId();
            if (!TeiloChat.formatMap.containsKey(targetUUID))
                sender.sendMessage("You don't have a custom name set.");
            else{
                HashMap<String, String> playerMap = TeiloChat.formatMap.get(targetUUID);
                if (!playerMap.containsKey("name"))
                    sender.sendMessage("You don't have a custom name set");
                else {
                    name = playerMap.get("name");
                    sender.sendMessage("Your name is set to: "+name);
                    sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', name));
                }
            }
            return 0;
        } else if (args.size()==2){
            if (!(sender instanceof Player)) return 3;
            targetUUID = ((Player) sender).getUniqueId();
            name = args.get(1);

            for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
                String playerName = player.getName();
                if (name.toLowerCase().contains(playerName.toLowerCase()))
                    return 4;
            }

            sender.sendMessage("Your name has been changed to: "+name);
            sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', name));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) return 2;
            OfflinePlayer player = getOfflinePlayer(args.get(1));
            if (player==null) return 1;
            targetUUID = player.getUniqueId();
            name = args.get(2);

            for (OfflinePlayer playerloop : Bukkit.getOfflinePlayers()){
                String playerName = playerloop.getName();
                if (name.toLowerCase().contains(playerName.toLowerCase()))
                    return 4;
            }

            sender.sendMessage(args.get(1)+"'s name has been changed to: "+name);
            sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', name));

        } else {
            return 1;
        }

        HashMap<String, String> playerMap = new HashMap<>();
        if (TeiloChat.formatMap.containsKey(targetUUID))
            playerMap = TeiloChat.formatMap.get(targetUUID);
        playerMap.put("name", name);
        TeiloChat.formatMap.put(targetUUID, playerMap);

        saveConfig();

        return 0;
    }

    public OfflinePlayer getOfflinePlayer(String name){
        OfflinePlayer player = null;
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
            if (offlinePlayer.getName()!=null && offlinePlayer.getName().equals(name)){
                player = offlinePlayer;
            }
        }
        return player;
    }

    public int setMsgPrefix(CommandSender sender, List<String> args){
        UUID targetUUID;
        String prefix;
        if (args.size()==1){
            if (!(sender instanceof Player)) return 3;
            targetUUID = ((Player) sender).getUniqueId();
            if (!TeiloChat.formatMap.containsKey(targetUUID))
                sender.sendMessage("You don't have a custom message prefix set.");
            else{
                HashMap<String, String> playerMap = TeiloChat.formatMap.get(targetUUID);
                if (!playerMap.containsKey("msgprefix"))
                    sender.sendMessage("You don't have a custom message prefix set");
                else {
                    prefix = playerMap.get("msgprefix");
                    sender.sendMessage("Your message prefix is set to: "+prefix);
                    sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', prefix));
                }
            }
            return 0;
        } else if (args.size()==2){
            if (!(sender instanceof Player)) return 3;
            targetUUID = ((Player) sender).getUniqueId();
            prefix = args.get(1);

            sender.sendMessage("Your message prefix has been changed to: "+prefix);
            sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', prefix));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) return 2;
            OfflinePlayer player = getOfflinePlayer(args.get(1));
            if (player==null) return 1;
            targetUUID = player.getUniqueId();
            prefix = args.get(2);

            sender.sendMessage(args.get(1)+"'s message prefix has been changed to: "+prefix);
            sender.sendMessage("With formatting: "+ ChatColor.translateAlternateColorCodes('&', prefix));

        } else {
            return 1;
        }

        HashMap<String, String> playerMap = new HashMap<>();
        if (TeiloChat.formatMap.containsKey(targetUUID))
            playerMap = TeiloChat.formatMap.get(targetUUID);
        playerMap.put("msgprefix", prefix);
        TeiloChat.formatMap.put(targetUUID, playerMap);

        saveConfig();

        return 0;
    }

    public int reset(CommandSender sender, LinkedList<String> args){
        if (args.size()==1){
            if (!(sender instanceof Player)) return 3;
            TeiloChat.formatMap.remove(((Player) sender).getUniqueId());
            sender.sendMessage("Your message prefix has been reset");
        }
        else if (args.size()==2){
            if (!sender.hasPermission("teilochat.other")) return 2;
            OfflinePlayer player = getOfflinePlayer(args.get(1));
            TeiloChat.formatMap.remove(player.getUniqueId());
            sender.sendMessage(args.get(1)+"'s message prefix has been reset");
        } else {
            return 1;
        }
        saveConfig();
        return 0;
    }

}
