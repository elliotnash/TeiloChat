package org.elliotnash.teilochat.core;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Sender {
    void send(Component content);
    @Deprecated
    void send(String message);
    boolean isConsole();
    UUID getUUID();
    String getName();
    boolean hasPermission(String permission);
    boolean isOffline();
}
