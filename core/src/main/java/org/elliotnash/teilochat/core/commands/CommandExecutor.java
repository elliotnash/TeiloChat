package org.elliotnash.teilochat.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.elliotnash.teilochat.core.ChatFormatter;
import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

import java.util.*;

public class CommandExecutor {

    private final ChatFormatter formatter = new ChatFormatter();
    private final CommandMessages messages = new CommandMessages();
    private final PlatformUtils platformUtils;
    private final ConfigManager config;

    public CommandExecutor(ConfigManager config, PlatformUtils platformUtils){
        this.config = config;
        this.platformUtils = platformUtils;
    }

    public boolean setName(Sender sender, List<String> args){
        if (args.size()==1){
            if (sender.isConsole()) {
                messages.sendConsole(sender);
                return false;
            }
            if (!config.contains(sender.getUUID()) || config.get(sender.getUUID()).name == null)
                sender.send("You don't have a custom name set.");
            else{
                PlayerFormat format = config.get(sender.getUUID());
                TextComponent nameComponent = formatter.format(format.name);
                sender.send("Your name is set to: "+format.name);
                sender.send(Component.text("With adventure formatting: ").append(nameComponent));
            }
        } else if (args.size()==2){
            if (sender.isConsole()) {
                messages.sendConsole(sender);
                return false;
            }
            String name = args.get(1);

            if (!platformUtils.uniqueName(name, sender)){
                messages.sendNameTaken(sender);
                return false;
            }

            TextComponent nameComponent = formatter.format(name);

            config.add(sender.getUUID(), new PlayerFormat());
            config.get(sender.getUUID()).name = name;
            config.write();

            sender.send("Your name has been changed to: "+name);
            sender.send(Component.text("With adventure formatting: ").append(nameComponent));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) {
                messages.sendMissingPerm(sender);
                return false;
            }
            Sender targetSender = platformUtils.getSenderFromName(args.get(1));
            if (targetSender == null){
                messages.sendInvalidUser(sender);
                return false;
            }
            String name = args.get(2);

            if (!platformUtils.uniqueName(name, sender)) {
                messages.sendNameTaken(sender);
                return false;
            }

            TextComponent nameComponent = formatter.format(name);

            config.add(targetSender.getUUID(), new PlayerFormat());
            config.get(targetSender.getUUID()).name = name;
            config.write();

            sender.send(args.get(1)+"'s name has been changed to: "+name);
            sender.send(Component.text("With adventure formatting: ").append(nameComponent));

            targetSender.send("Your name has been changed to: "+name);
            targetSender.send(Component.text("With adventure formatting: ").append(nameComponent));

        } else {
            messages.sendInvalidCommand(sender);
            return false;
        }
        return true;
    }

    public boolean setMsgPrefix(Sender sender, List<String> args){
        if (args.size()==1){
            if (sender.isConsole()) {
                messages.sendConsole(sender);
                return false;
            }
            if (!config.contains(sender.getUUID()) || config.get(sender.getUUID()).msgprefix == null)
                sender.send("You don't have a custom message prefix set.");
            else{
                PlayerFormat format = config.get(sender.getUUID());
                TextComponent prefixComponent = formatter.format(format.msgprefix);
                sender.send("Your message prefix is set to: \""+format.msgprefix+"\"");
                sender.send(Component.text("With adventure formatting: ").append(prefixComponent));
            }
        } else if (args.size()==2){
            if (sender.isConsole()) {
                messages.sendConsole(sender);
                return false;
            }
            String msgprefix = args.get(1);

            config.add(sender.getUUID(), new PlayerFormat());
            config.get(sender.getUUID()).msgprefix = msgprefix;
            config.write();

            TextComponent prefixComponent = formatter.format(msgprefix);
            sender.send("Your message prefix has been changed to: "+msgprefix);
            sender.send(Component.text("With adventure formatting: ").append(prefixComponent));

        } else if (args.size()==3){
            if (!sender.hasPermission("teilochat.other")) {
                messages.sendMissingPerm(sender);
                return false;
            }
            Sender targetSender = platformUtils.getSenderFromName(args.get(1));
            if (targetSender == null){
                messages.sendInvalidUser(sender);
                return false;
            }
            String msgprefix = args.get(2);

            config.add(targetSender.getUUID(), new PlayerFormat());
            config.get(targetSender.getUUID()).msgprefix = msgprefix;
            config.write();

            TextComponent prefixComponent = formatter.format(msgprefix);
            sender.send(args.get(1)+"'s message prefix has been changed to: "+msgprefix);
            sender.send(Component.text("With adventure formatting: ").append(prefixComponent));

        } else {
            messages.sendInvalidCommand(sender);
            return false;
        }
        return true;
    }

    public boolean reset(Sender sender, List<String> args){
        if (args.size()==1){
            if (sender.isConsole()) {
                messages.sendConsole(sender);
                return false;
            }
            config.remove(sender.getUUID());
            config.write();
            sender.send("Your name and message prefix have been reset");
        }
        else if (args.size()==2){
            if (!sender.hasPermission("teilochat.other")) {
                messages.sendMissingPerm(sender);
                return false;
            }
            Sender targetSender = platformUtils.getSenderFromName(args.get(1));
            if (targetSender == null){
                messages.sendInvalidUser(sender);
                return false;
            }
            config.remove(targetSender.getUUID());
            config.write();
            sender.send(args.get(1)+"'s name and message prefix have been reset");
        } else {
            messages.sendInvalidCommand(sender);
            return false;
        }
        return true;
    }
}
