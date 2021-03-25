package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;

import java.util.Optional;

public class VelocityUtils implements PlatformUtils {

    private final ProxyServer server;

    public VelocityUtils(ProxyServer server) {
        this.server = server;
    }

    @Override
    public boolean uniqueName(String name, Sender sender) {
        return true;
    }

    @Override
    public Sender getSenderFromName(String name) {
        Optional<Player> player = server.getPlayer(name);
        return player.map(VelocitySender::new).orElse(null);
    }
}
