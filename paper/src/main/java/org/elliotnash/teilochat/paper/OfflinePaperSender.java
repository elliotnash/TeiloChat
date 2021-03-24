package org.elliotnash.teilochat.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.elliotnash.teilochat.core.Sender;

import java.util.UUID;

public class OfflinePaperSender implements Sender {

    private final OfflinePlayer player;

    public OfflinePaperSender(OfflinePlayer player){
        this.player = player;
    }

    @Override
    public boolean isOffline(){return true;}

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public void send(Component content) {
        if (player.isOnline()){
            player.getPlayer().sendMessage(content);
        }
    }

    @Override
    public void send(String message) {
        if (player.isOnline()){
            player.getPlayer().sendMessage(message);
        }
    }

    @Override
    public String getName(){
        return player.getName();
    }

    @Override
    public boolean hasPermission(String permission){
        return false;
    }

}
