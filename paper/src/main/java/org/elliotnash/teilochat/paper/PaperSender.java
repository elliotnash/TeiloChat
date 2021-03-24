package org.elliotnash.teilochat.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elliotnash.teilochat.core.Sender;

import java.util.UUID;

public class PaperSender implements Sender {

    private final CommandSender sender;

    public PaperSender(CommandSender sender){
        this.sender = sender;
    }

    @Override
    public boolean isConsole() {
        return !(sender instanceof Player);
    }

    @Override
    public UUID getUUID() {
        return isConsole() ? null: ((Player) sender).getUniqueId();
    }

    @Override
    public void send(Component content) {
        sender.sendMessage(content);
    }

    @Override
    public void send(String message) {
        sender.sendMessage(message);
    }

    @Override
    public String getName(){
        return sender.getName();
    }

    @Override
    public boolean hasPermission(String permission){
        return sender.hasPermission(permission);
    }

}
