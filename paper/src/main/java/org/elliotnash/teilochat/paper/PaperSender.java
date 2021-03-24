package org.elliotnash.teilochat.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elliotnash.teilochat.core.player.Sender;

import java.util.UUID;

public class PaperSender implements Sender {

    private final CommandSender sender;

    @Override
    public boolean isConsole() {
        return !(sender instanceof Player);
    }

    @Override
    public UUID getUUID() {
        return isConsole() ? null: ((Player) sender).getUniqueId();
    }

    public PaperSender(CommandSender sender){
        this.sender = sender;
    }

    @Override
    public void send(Component content) {
        sender.sendMessage(content);
    }

    @Override
    public void send(String message) {
        sender.sendMessage(message);
    }

}
