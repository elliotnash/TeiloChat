package org.elliotnash.teilochat.paper.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;

public class ChatListener {
    @Subscribe
    public void onPlayerChat(PlayerChatEvent event){
        System.out.println("PLAYER CHATTED");
    }
}
