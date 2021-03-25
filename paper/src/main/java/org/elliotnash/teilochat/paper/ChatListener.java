package org.elliotnash.teilochat.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.elliotnash.teilochat.core.ChatFormatter;
import org.elliotnash.teilochat.core.chat.ChatHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

public class ChatListener implements Listener{

    ChatHandler chat;

    public ChatListener(ConfigManager config){
        this.chat = new ChatHandler(config);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnMessage(AsyncPlayerChatEvent event) {

        Component message = chat.message(new PaperSender(event.getPlayer()), event.getMessage());

        event.getRecipients().forEach((player)->{player.sendMessage(message);});

        Bukkit.getConsoleSender().sendMessage(Component.text()
                .append(MiniMessage.get().parse("<rainbow>["+event.getPlayer().getName()+"] </rainbow>"))
                .append(message));

        event.setCancelled(true);

    }

//    @EventHandler
//    public void OnPlayerLogin(PlayerLoginEvent event){
//        String name = event.getPlayer().getName();
//        if (config.contains(event.getPlayer().getUniqueId())){
//            PlayerFormat format = config.get(event.getPlayer().getUniqueId());
//            if (format.name != null) name = format.name;
//        }
//        TextComponent nameComponent = formatter.format(name);
//
//        event.getPlayer().displayName(nameComponent);
//        event.getPlayer().playerListName(nameComponent);
//
//    }

    //events to handle colourizing join+quit messages
    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent event){
        event.joinMessage(chat.join(new PaperSender(event.getPlayer())));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerQuit(PlayerQuitEvent event){
        event.quitMessage(chat.leave(new PaperSender(event.getPlayer())));
    }

}
