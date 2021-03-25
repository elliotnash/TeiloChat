package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.elliotnash.teilochat.core.chat.ChatHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;

public class ChatListener {

    private final ProxyServer server;
    private final ChatHandler chat;

    public ChatListener(ProxyServer server, ConfigManager config){
        this.server = server;
        this.chat = new ChatHandler(config);
    }

    @Subscribe(order = PostOrder.LAST)
    public void onPlayerChat(PlayerChatEvent event){
        Component message = chat.message(new VelocitySender(event.getPlayer()), event.getMessage());
        // send message to players
        server.getAllPlayers().forEach((player) -> {player.sendMessage(message);});
        // send message to console
        server.getConsoleCommandSource().sendMessage(
            MiniMessage.get().parse(
                "<rainbow>["+event.getPlayer().getCurrentServer().get().getServerInfo().getName()+"]"+
                "["+event.getPlayer().getUsername()+"] </rainbow>")
            .append(message)
        );

        event.setResult(PlayerChatEvent.ChatResult.denied());
    }

    @Subscribe(order = PostOrder.LAST)
    public void onJoin(PostLoginEvent event){
        Component joinMessage = chat.join(new VelocitySender(event.getPlayer()));
        server.sendMessage(joinMessage);
    }

    @Subscribe(order = PostOrder.LAST)
    public void onLeave(DisconnectEvent event){
        Component leaveMessage = chat.leave(new VelocitySender(event.getPlayer()));
        server.sendMessage(leaveMessage);
    }
}
