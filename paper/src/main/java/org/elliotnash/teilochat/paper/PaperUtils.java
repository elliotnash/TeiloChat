package org.elliotnash.teilochat.paper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class PaperUtils implements PlatformUtils {
    @Override
    public boolean uniqueName(String name, Sender sender) {
        for (OfflinePlayer playerLoop : Bukkit.getOfflinePlayers()){
            String playerName = playerLoop.getName();
            if (playerName.equals(sender.getName()))
                continue;
            if (name.toLowerCase().contains(playerName.toLowerCase()))
                return false;
        }
        return true;
    }
    @Override
    public Sender getSenderFromName(String name){
        OfflinePlayer offlinePlayer = getOfflinePlayer(name);
        if (!offlinePlayer.hasPlayedBefore()){
            return null;
        }
        return new PaperSender((CommandSender) offlinePlayer);
    }
}
