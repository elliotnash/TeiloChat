package org.elliotnash.teilochat.core.config;

import java.util.HashMap;
import java.util.UUID;

class TeiloChatConfig {
    public String defaultMsgPrefix = ": ";
    public HashMap<UUID, PlayerFormat> playerFormats = new HashMap<>();
}
