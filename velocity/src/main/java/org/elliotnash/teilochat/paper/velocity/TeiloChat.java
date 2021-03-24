package org.elliotnash.teilochat.paper.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "teilochat",
        name = "TeiloChat-Velocity",
        version = "2.0-SNAPSHOT",
        description = "Add chat customization",
        url = "https://github.com/elliotnash/TeiloChat",
        authors = {"Elliot Nash"}
)
public class TeiloChat {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("PLUGIN LOADED");
    }

    public void onChat(){

    }
}
