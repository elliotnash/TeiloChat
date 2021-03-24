package org.elliotnash.teilochat.core.commands;

import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;
import org.elliotnash.teilochat.core.config.ConfigManager;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    CommandExecutor executor;
    CommandMessages messages = new CommandMessages();

    public CommandHandler(ConfigManager config, PlatformUtils platformUtils){
        this.executor = new CommandExecutor(config, platformUtils);
    }

    public LinkedList<String> parser(String args){
        //parse by spaces, but also also keep quotations
        LinkedList<String> argList = new LinkedList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args);
        while (m.find())
            argList.add(m.group(1).replace("\"", ""));
        return argList;
    }

    public void command(Sender sender, String args){

        LinkedList<String> argList = parser(args);

        if (argList.size() >= 1 && argList.size() <= 3){
            switch (argList.get(0)){
                case "name":
                    executor.setName(sender, argList);
                    break;
                case "msgprefix":
                    executor.setMsgPrefix(sender, argList);
                    break;
                case "reset":
                    executor.reset(sender, argList);
                    break;
                default:
                    messages.sendInvalidCommand(sender);
                    break;
            }
        } else {
            messages.sendInvalidCommand(sender);
        }

    }

}
