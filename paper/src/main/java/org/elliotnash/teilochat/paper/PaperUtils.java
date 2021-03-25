package org.elliotnash.teilochat.paper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getOfflinePlayers;

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
        OfflinePlayer player = null;
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
            if (offlinePlayer.getName()!=null && offlinePlayer.getName().equals(name)){
                player = offlinePlayer;
            }
        }
        if (player == null)
            return null;
        if (player.isOnline()){
            return new PaperSender(player.getPlayer());
        } else {
            return new OfflinePaperSender(player.getPlayer());
        }
    }
    @Override
    public List<Sender> getAllSenders(){
        return Arrays.stream(getOfflinePlayers()).map(OfflinePaperSender::new)
                .collect(Collectors.toList());
    }
}
