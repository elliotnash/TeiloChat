package org.elliotnash.teilochat.paper.velocity;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.elliotnash.teilochat.core.Sender;

import java.util.UUID;

public class VelocitySender implements Sender {

    private final Player player;

    public VelocitySender(Player player){
        this.player = player;
    }


    @Override
    public void send(Component content) {
        player.sendMessage(content);
    }

    @Override
    public void send(String message) {
        player.sendMessage(MiniMessage.get().parse(message));
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getUsername();
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean isOffline() {
        return false;
    }
}
