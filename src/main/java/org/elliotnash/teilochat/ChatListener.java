package org.elliotnash.teilochat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;

public class ChatListener implements Listener{
    @EventHandler
    public void OnMessage(PlayerChatEvent event) {
        String name = event.getPlayer().getName();
        String msgPrefix = "&r: ";
        if (TeiloChat.formatMap.containsKey(event.getPlayer().getUniqueId())){
            HashMap<String, String> playerMap = TeiloChat.formatMap.get(event.getPlayer().getUniqueId());
            if (playerMap.containsKey("name")) name = playerMap.get("name");
            if (playerMap.containsKey("msgprefix")) msgPrefix = playerMap.get("msgprefix");
        }

        String format = ChatColor.translateAlternateColorCodes('&', name+msgPrefix+event.getMessage());
        event.setFormat(format);
    }
}
