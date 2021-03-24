package org.elliotnash.teilochat.core.player;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Sender {
    void send(Component content);
    @Deprecated
    void send(String message);
    boolean isConsole();
    UUID getUUID();
}
