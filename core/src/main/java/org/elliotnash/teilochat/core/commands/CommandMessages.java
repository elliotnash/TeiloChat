package org.elliotnash.teilochat.core.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.elliotnash.teilochat.core.Sender;

public class CommandMessages {
    public void sendHelp(Sender sender){
        sender.send("/tc name <name> sets your name");
        sender.send("/tc msgprefix <prefix> sets the prefix before your message");
        sender.send("/tc reset resets your chat customization");
        sender.send("If you need to include spaces in your name or prefix, please surround it with quotes");
        sender.send("ie. /tc name \"Elliot Nash\" would set my name to Elliot Nash");
    }
    public void sendInvalidCommand(Sender sender){
        sender.send(MiniMessage.get().parse("<red>Invalid command!"));
        sendHelp(sender);
    }
    public void sendMissingPerm(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You are missing permission to set other people's names!"));
    }
    public void sendConsole(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You can't run this message from console!"));
    }
    public void sendNameTaken(Sender sender){
        sender.send(MiniMessage.get().parse("<red>You can't set your name to another users' name!"));
    }
    public void sendInvalidUser(Sender sender){
        sender.send(MiniMessage.get().parse("<red>Invalid user!"));
    }
}
