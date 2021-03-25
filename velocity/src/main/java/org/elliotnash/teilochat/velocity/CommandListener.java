package org.elliotnash.teilochat.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.elliotnash.teilochat.core.commands.CommandHandler;
import org.elliotnash.teilochat.core.config.ConfigManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CommandListener implements RawCommand {

    private final CommandHandler handler;

    public CommandListener(ProxyServer server, ConfigManager config){
        handler = new CommandHandler(config, new VelocityUtils(server));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation){
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        new Thread(()->{
            List<String> completions = handler.completion(new VelocitySender(invocation.source()), invocation.arguments());
            future.complete(completions);
        }).start();
        return future;
    }

    @Override
    public void execute(Invocation invocation) {
        handler.command(new VelocitySender(invocation.source()), invocation.arguments());
    }

}
