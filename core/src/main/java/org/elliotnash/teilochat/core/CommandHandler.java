package org.elliotnash.teilochat.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;
import org.elliotnash.teilochat.core.player.Sender;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    ChatFormatter formatter = new ChatFormatter();
    ConfigManager config;

    public CommandHandler(ConfigManager config){
        this.config = config;
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

    public int setName(Sender sender, List<String> args){
        UUID targetUUID;
        String name;
        if (args.size()==1){
            if (sender.isConsole()) return 3;
            targetUUID = sender.getUUID();
            if (config.contains(targetUUID))
                sender.send("You don't have a custom name set.");
            else{
                PlayerFormat format = config.get(targetUUID);
                if (format.name!=null)
                    sender.send("You don't have a custom name set");
                else {
                    TextComponent nameComponent = formatter.format(format.name);
                    sender.send("Your name is set to: "+format.name);
                    sender.send(Component.text("With adventure formatting: ").append(nameComponent));
                }
            }
            return 0;
        } else if (args.size()==2){
            if (sender.isConsole()) return 3;
            Player player = ((Player) sender);
            targetUUID = player.getUniqueId();
            name = args.get(1);

            for (OfflinePlayer playerLoop : Bukkit.getOfflinePlayers()){
                String playerName = playerLoop.getName();
                if (playerName.equals(((Player) sender).getName()))
                    continue;
                if (name.toLowerCase().contains(playerName.toLowerCase()))
                    return 4;
            }

            TextComponent nameComponent = formatter.format(name);

            player.displayName(nameComponent);
            player.playerListName(nameComponent);

            sender.sendMessage("Your name has been changed to: "+name);
            player.sendMessage(Component.text("With adventure formatting: ").append(nameComponent));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) return 2;
            OfflinePlayer offlinePlayer = getOfflinePlayer(args.get(1));
            if (offlinePlayer==null) return 1;
            targetUUID = offlinePlayer.getUniqueId();
            name = args.get(2);

            for (OfflinePlayer playerLoop : Bukkit.getOfflinePlayers()){
                String playerName = playerLoop.getName();
                if (playerName.equals(((Player) sender).getName()))
                    continue;
                if (name.toLowerCase().contains(playerName.toLowerCase()))
                    return 4;
            }

            TextComponent nameComponent = formatter.format(name);


            if (offlinePlayer.isOnline()){
                Player player = offlinePlayer.getPlayer();
                player.displayName(nameComponent);
                player.playerListName(nameComponent);
            }

            sender.sendMessage(args.get(1)+"'s name has been changed to: "+name);
            sender.sendMessage(Component.text("With adventure formatting: ").append(nameComponent));

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

                    TextComponent prefixComponent = formatter.format(prefix);
                    sender.sendMessage("Your message prefix is set to: "+prefix);
                    sender.sendMessage(Component.text("With adventure formatting: ").append(prefixComponent));
                }
            }
            return 0;
        } else if (args.size()==2){
            if (!(sender instanceof Player)) return 3;
            targetUUID = ((Player) sender).getUniqueId();
            prefix = args.get(1);

            TextComponent prefixComponent = formatter.format(prefix);
            sender.sendMessage("Your message prefix has been changed to: "+prefix);
            sender.sendMessage(Component.text("With adventure formatting: ").append(prefixComponent));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) return 2;
            OfflinePlayer player = getOfflinePlayer(args.get(1));
            if (player==null) return 1;
            targetUUID = player.getUniqueId();
            prefix = args.get(2);

            TextComponent prefixComponent = formatter.format(prefix);
            sender.sendMessage(args.get(1)+"'s message prefix has been changed to: "+prefix);
            sender.sendMessage(Component.text("With adventure formatting: ").append(prefixComponent));

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
