package org.elliotnash.teilochat.paper.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import org.elliotnash.teilochat.core.chat.ChatHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;

public class ChatListener {

    ChatHandler chat;

    public ChatListener(ConfigManager config){
        this.chat = new ChatHandler(config);
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        System.out.println("PLAYER CHATTED");
        event.setResult(PlayerChatEvent.ChatResult.denied());
    }
}
