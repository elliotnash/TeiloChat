package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import org.elliotnash.teilochat.core.commands.CommandHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;

public final class CommandListener implements RawCommand {

    private final CommandHandler handler;

    public CommandListener(ProxyServer server, ConfigManager config){
        handler = new CommandHandler(config, new VelocityUtils(server));
    }

    @Override
    public void execute(Invocation invocation) {
        System.out.println(invocation.arguments());
        handler.command(new VelocitySender(invocation.source()), invocation.arguments());
    }

}
