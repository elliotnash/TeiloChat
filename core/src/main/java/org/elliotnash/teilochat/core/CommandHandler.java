package org.elliotnash.teilochat.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    ChatFormatter formatter = new ChatFormatter();
    PlatformUtils platformUtils;
    ConfigManager config;

    public CommandHandler(ConfigManager config, PlatformUtils platformUtils){
        this.config = config;
        this.platformUtils = platformUtils;
    }


    public void sendHelp(Sender sender){
        sender.send("/tc name <name> sets your name");
        sender.send("/tc msgprefix <prefix> sets the prefix before your message");
        sender.send("/tc reset resets your chat customization");
        sender.send("If you need to include spaces in your name or prefix, please surround it with quotes");
        sender.send("ie. /tc name \"Elliot Nash\" would set my name to Elliot Nash");
    }
    public void sendInvalid(Sender sender){
        sender.send(MiniMessage.get().parse("<red>Invalid command!"));
        sendHelp(sender);
    }
    public void sendMissingPerm(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You are missing permission to set other people's names!"));
    }
    public void sendConsole(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You can't run this message from console!"));
    }
    public void sendNameTaken(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You can't set your name to another users' name!"));
    }
    public void sendInvalidUser(Sender sender){
        sender.send(MiniMessage.get().parse("<red>Invalid user!"));
    }


    public LinkedList<String> parser(String args){
        //convert arg array to String
//        StringBuilder argStringBuilder = new StringBuilder();
//        for (String str : args){
//            argStringBuilder.append(str).append(" ");
//        }
//        argStringBuilder.setLength(argStringBuilder.length()-1);
//        String argString = argStringBuilder.toString();

        //parse by spaces, but also also keep quotations
        LinkedList<String> argList = new LinkedList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args);
        while (m.find())
            argList.add(m.group(1).replace("\"", ""));
        return argList;
    }

    public boolean setName(Sender sender, String argsStr){
        LinkedList<String> args = parser(argsStr);
        String name;
        if (args.size()==1){
            if (sender.isConsole()) return 3;
            if (config.contains(sender.getUUID()))
                sender.send("You don't have a custom name set.");
            else{
                PlayerFormat format = config.get(sender.getUUID());
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
            name = args.get(1);

            if (platformUtils.uniqueName(name, sender))
                return 4;

            TextComponent nameComponent = formatter.format(name);

            config.add(sender.getUUID(), new PlayerFormat());
            config.get(sender.getUUID()).name = name;
            config.write();

            sender.send("Your name has been changed to: "+name);
            sender.send(Component.text("With adventure formatting: ").append(nameComponent));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) return 2;
            Sender targetSender = platformUtils.getSenderFromName(args.get(1));
            if (targetSender == null)
                return 5;
            name = args.get(2);

            if (platformUtils.uniqueName(name, sender))
                return 4;

            TextComponent nameComponent = formatter.format(name);

            config.add(targetSender.getUUID(), new PlayerFormat());
            config.get(targetSender.getUUID()).name = name;
            config.write();

            sender.send(args.get(1)+"'s name has been changed to: "+name);
            sender.send(Component.text("With adventure formatting: ").append(nameComponent));

            targetSender.send("Your name has been changed to: "+name);
            targetSender.send(Component.text("With adventure formatting: ").append(nameComponent));

        } else {
            return 1;
        }
        return true;
    }

    public int setMsgPrefix(Sender sender, String argsStr){
        LinkedList<String> args = parser(argsStr);
        String prefix;
        if (args.size()==1){
            if (sender.isConsole()) return 3;
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
