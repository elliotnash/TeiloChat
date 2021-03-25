package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public List<Sender> getAllSenders(){
        return server.getAllPlayers().stream().map(((Function<Player, Sender>) VelocitySender::new))
                .collect(Collectors.toList());
    }

}
