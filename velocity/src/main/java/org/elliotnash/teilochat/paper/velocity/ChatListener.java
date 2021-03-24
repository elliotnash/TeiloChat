package org.elliotnash.teilochat.paper.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
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
        server.sendMessage(message);
        event.setResult(PlayerChatEvent.ChatResult.denied());
    }

    @Subscribe(order = PostOrder.LAST)
    public void onJoin(PostLoginEvent event){
        Component joinMessage = chat.join(new VelocitySender(event.getPlayer()));
        server.sendMessage(joinMessage);
    }
}
