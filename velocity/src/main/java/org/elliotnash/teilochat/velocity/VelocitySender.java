package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.elliotnash.teilochat.core.Sender;

import java.util.UUID;

public class VelocitySender implements Sender {

    private final CommandSource source;

    public VelocitySender(CommandSource source){
        this.source = source;
    }


    @Override
    public void send(Component content) {
        source.sendMessage(content);
    }

    @Override
    public void send(String message) {
        source.sendMessage(MiniMessage.get().parse(message));
    }

    @Override
    public boolean isConsole() {
        return !(source instanceof Player);
    }

    @Override
    public UUID getUUID() {
        if (!isConsole())
            return ((Player) source).getUniqueId();
        return null;
    }

    @Override
    public String getName() {
        if (!isConsole())
            return ((Player) source).getUsername();
        return null;
    }

    @Override
    public boolean hasPermission(String permission) {
        return source.hasPermission(permission);
    }

    @Override
    public boolean isOffline() {
        return false;
    }
}
