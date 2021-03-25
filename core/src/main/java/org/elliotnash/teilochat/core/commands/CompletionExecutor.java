package org.elliotnash.teilochat.core.commands;

import org.elliotnash.teilochat.core.Sender;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompletionExecutor {

    public List<String> completion(Sender sender, List<String> args) {



        List<String> commands = new LinkedList<String>();
        List<String> completions = new LinkedList<String>();

        switch (args.size()){
            case 1:
                commands = new LinkedList<String>(Arrays.asList("name", "msgprefix", "reset"));
                completions = commands.stream()
                    .filter(name -> name.regionMatches(true, 0, args.get(0), 0, args.get(0).length()))
                    .collect(Collectors.toList());
                break;
            case 2:
                if (sender.hasPermission("teilochat.other")){
//                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
//                        commands.add(player.getName());
//                    }
                    completions = commands.stream()
                            .filter(name -> name.regionMatches(true, 0, args.get(1), 0, args.get(1).length()))
                            .collect(Collectors.toList());
                }
                break;
        }

        Collections.sort(completions);
        return completions;

    }
}
