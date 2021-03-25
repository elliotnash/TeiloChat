package org.elliotnash.teilochat.core.commands;

import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;
import org.elliotnash.teilochat.core.config.ConfigManager;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    CommandExecutor commandExecutor;
    CompletionExecutor completionExecutor = new CompletionExecutor();
    CommandMessages messages = new CommandMessages();

    public CommandHandler(ConfigManager config, PlatformUtils platformUtils){
        this.commandExecutor = new CommandExecutor(config, platformUtils);
    }

    public List<String> parser(String args){
        //parse by spaces, but also also keep quotations
        LinkedList<String> argList = new LinkedList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(args);
        while (m.find())
            argList.add(m.group(1).replace("\"", ""));
        return argList;
    }

    public List<String> completion(Sender sender, String args){
        List<String> argsList = parser(args);
        if (args.endsWith(" ") || args.isEmpty())
            argsList.add("");
        return completionExecutor.completion(sender, argsList);
    }

    public void command(Sender sender, String args){

        List<String> argList = parser(args);

        if (argList.size() >= 1 && argList.size() <= 3){
            switch (argList.get(0)){
                case "name":
                    commandExecutor.setName(sender, argList);
                    break;
                case "msgprefix":
                    commandExecutor.setMsgPrefix(sender, argList);
                    break;
                case "reset":
                    commandExecutor.reset(sender, argList);
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
