package org.elliotnash.teilochat;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener{

    ChatFormatter formatter = new ChatFormatter();
    BukkitAudiences bukkitAudiences = BukkitAudiences.create(TeiloChat.plugin);

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnMessage(AsyncPlayerChatEvent event) {
        String name = event.getPlayer().getName();
        String msgPrefix = "&r: ";
        String message = event.getMessage();
        if (TeiloChat.formatMap.containsKey(event.getPlayer().getUniqueId())){
            HashMap<String, String> playerMap = TeiloChat.formatMap.get(event.getPlayer().getUniqueId());
            if (playerMap.containsKey("name")) name = playerMap.get("name");
            if (playerMap.containsKey("msgprefix")) msgPrefix = playerMap.get("msgprefix");
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

        System.out.println(recipients);

        for (Player player : recipients){
            Audience audience = bukkitAudiences.player(player);
            audience.sendMessage(finalComponent);
        }

    }

    @EventHandler
    public void OnPlayerLogin(PlayerLoginEvent event){
        String name = event.getPlayer().getName();
        if (TeiloChat.formatMap.containsKey(event.getPlayer().getUniqueId())){
            HashMap<String, String> playerMap = TeiloChat.formatMap.get(event.getPlayer().getUniqueId());
            if (playerMap.containsKey("name")) name = playerMap.get("name");
        }
        name = LegacyComponentSerializer.legacySection().serialize(formatter.format(name));

        event.getPlayer().setDisplayName(name);
        event.getPlayer().setPlayerListName(name);

    }

    //events to handle colourizing join+quit messages
    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
        String name = ChatColor.YELLOW+event.getPlayer().getName();
        if (TeiloChat.formatMap.containsKey(event.getPlayer().getUniqueId())) {
            HashMap<String, String> playerMap = TeiloChat.formatMap.get(event.getPlayer().getUniqueId());
            if (playerMap.containsKey("name")) name = playerMap.get("name");
        }

        TextComponent message = Component.text()
                .append(formatter.format(name))
                .append(Component.text().color(NamedTextColor.YELLOW).content(" joined the game")).build();

        bukkitAudiences.players().sendMessage(message);
        bukkitAudiences.console().sendMessage(message);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
        String name = ChatColor.YELLOW+event.getPlayer().getName();
        if (TeiloChat.formatMap.containsKey(event.getPlayer().getUniqueId())) {
            HashMap<String, String> playerMap = TeiloChat.formatMap.get(event.getPlayer().getUniqueId());
            if (playerMap.containsKey("name")) name = playerMap.get("name");
        }

        TextComponent message = Component.text()
                .append(formatter.format(name))
                .append(Component.text().color(NamedTextColor.YELLOW).content(" left the game")).build();

        bukkitAudiences.players().sendMessage(message);
        bukkitAudiences.console().sendMessage(message);
    }

}
