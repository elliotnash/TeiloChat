package org.elliotnash.teilochat.core.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.elliotnash.teilochat.core.ChatFormatter;
import org.elliotnash.teilochat.core.Sender;
import org.elliotnash.teilochat.core.config.ConfigManager;
import org.elliotnash.teilochat.core.config.PlayerFormat;

import java.util.HashSet;
import java.util.Set;

public class ChatHandler {

    private final ChatFormatter formatter = new ChatFormatter();
    private final ConfigManager config;

    public ChatHandler(ConfigManager config){
        this.config = config;
    }

    public Component message(Sender sender, String message){
        String name = sender.getName();
        String msgPrefix = "&r: ";
        if (config.contains(sender.getUUID())){
            PlayerFormat format = config.get(sender.getUUID());
            if (format.name != null) name = format.name;
            if (format.msgprefix != null) msgPrefix = format.msgprefix;
        }

        //get text components
        TextComponent nameComponent = formatter.format(name);
        TextComponent messageComponent = formatter.format(msgPrefix+message);

        return nameComponent.append(messageComponent);
    }

    public Component join(Sender sender){
        String name = "&e"+sender.getName();
        if (config.contains(sender.getUUID())) {
            PlayerFormat format = config.get(sender.getUUID());
            if (format.name != null) name = format.name;
        }

        return Component.text()
                .append(formatter.format(name))
                .append(MiniMessage.get().parse("<yellow> joined the game")).build();
    }

    public Component leave(Sender sender){
        String name = "&e"+sender.getName();
        if (config.contains(sender.getUUID())) {
            PlayerFormat format = config.get(sender.getUUID());
            if (format.name != null) name = format.name;
        }

        return Component.text()
                .append(formatter.format(name))
                .append(MiniMessage.get().parse("<yellow> left the game")).build();
    }

}
