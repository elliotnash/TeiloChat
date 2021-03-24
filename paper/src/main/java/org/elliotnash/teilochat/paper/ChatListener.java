package org.elliotnash.teilochat.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

public class ChatListener implements Listener{

    ChatFormatter formatter = new ChatFormatter();
    ConfigManager config;

    public ChatListener(ConfigManager config){
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnMessage(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName();
        String msgPrefix = "&r: ";
        String message = event.getMessage();
        if (config.contains(event.getPlayer().getUniqueId())){
            PlayerFormat format = config.get(event.getPlayer().getUniqueId());
            if (format.name != null) name = format.name;
            if (format.msgprefix != null) msgPrefix = format.msgprefix;
        }

        //clear message recipients so it only goes to console
        //but save first to use later
        Set<Player> recipients = new HashSet<Player>(event.getRecipients());
        event.getRecipients().clear();

        //get text components
        TextComponent nameComponent = formatter.format(name);
        TextComponent messageComponent = formatter.format(msgPrefix+message);

        event.setMessage(LegacyComponentSerializer.legacySection().serialize(messageComponent));
        event.setFormat("%1$s%2$s");

        TextComponent finalComponent = nameComponent.append(messageComponent);

        for (Player player : recipients){
            player.sendMessage(finalComponent);
        }

    }

    @EventHandler
    public void OnPlayerLogin(PlayerLoginEvent event){
        String name = event.getPlayer().getName();
        if (config.contains(event.getPlayer().getUniqueId())){
            PlayerFormat format = config.get(event.getPlayer().getUniqueId());
            if (format.name != null) name = format.name;
        }
        TextComponent nameComponent = formatter.format(name);

        event.getPlayer().displayName(nameComponent);
        event.getPlayer().playerListName(nameComponent);

    }

    //events to handle colourizing join+quit messages
    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent event){
        String name = ChatColor.YELLOW+event.getPlayer().getName();
        if (config.contains(event.getPlayer().getUniqueId())) {
            PlayerFormat format = config.get(event.getPlayer().getUniqueId());
            if (format.name != null) name = format.name;
        }

        TextComponent message = Component.text()
                .append(formatter.format(name))
                .append(Component.text().color(NamedTextColor.YELLOW).content(" joined the game")).build();

        event.joinMessage(message);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerQuit(PlayerQuitEvent event){
        String name = ChatColor.YELLOW+event.getPlayer().getName();
        if (config.contains(event.getPlayer().getUniqueId())) {
            PlayerFormat format = config.get(event.getPlayer().getUniqueId());
            if (format.name != null) name = format.name;
        }

        TextComponent message = Component.text()
                .append(formatter.format(name))
                .append(Component.text().color(NamedTextColor.YELLOW).content(" left the game")).build();

        event.quitMessage(message);

    }

}
