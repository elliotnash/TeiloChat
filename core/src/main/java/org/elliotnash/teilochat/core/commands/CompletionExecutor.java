package org.elliotnash.teilochat.core.commands;

import org.elliotnash.teilochat.core.PlatformUtils;
import org.elliotnash.teilochat.core.Sender;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CompletionExecutor {

    private final PlatformUtils platformUtils;

    public CompletionExecutor(PlatformUtils platformUtils){
        this.platformUtils = platformUtils;
    }

    private List<String> getPartialMatches(List<String> list, String match){
        return list.stream()
                .filter(name -> name.regionMatches(true, 0, match, 0, match.length()))
                .collect(Collectors.toList());
    }

    public List<String> completion(Sender sender, List<String> args) {

        List<String> commands;
        List<String> completions = new LinkedList<String>();

        switch (args.size()){
            case 1:
                commands = new LinkedList<String>(Arrays.asList("name", "msgprefix", "reset"));
                completions = getPartialMatches(commands, args.get(0));
                break;
            case 2:
                if (sender.hasPermission("teilochat.other")) {
                    commands = platformUtils.getAllSenders().stream().map(Sender::getName)
                            .collect(Collectors.toList());
                    completions = getPartialMatches(commands, args.get(1));
                }
                break;
        }

        Collections.sort(completions);
        return completions;

    }
}
