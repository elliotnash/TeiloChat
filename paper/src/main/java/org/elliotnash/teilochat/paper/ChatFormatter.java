package org.elliotnash.teilochat.paper;


import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Random;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFormatter {

    Pattern colorPattern = Pattern.compile("(&[0-9a-fA-FrR]|&#[0-9a-fA-F]{6}|&%.+%)");

    public TextComponent format(String string){
        StringBuilder sb = new StringBuilder(string);
        sb.append("&r");
        for (int i = 0; i < sb.length(); i++){
            if (sb.charAt(i)=='&' && i+7 < sb.length() && sb.charAt(i+1)=='%'){
                int firstpc = i+1;
                int lastpc = -1;
                for (int j = i+2; j < sb.length(); j++){
                    if (sb.charAt(j)=='%'){
                        lastpc = j;
                        break;
                    }
                }
                if (lastpc!=-1){
                    String[] colorArr = sb.substring(firstpc+1, lastpc).split("(?=&)");
                    EloitList colorList = new EloitList();
                    if (colorList.setMode(colorArr[0].charAt(0))){
                        for (int j = 1; j < colorArr.length; j++)
                            colorList.add(colorArr[j]);

                        int resetIndex = lastpc+indexOf(colorPattern, sb.substring(lastpc, sb.length()))-1;

                        for (;resetIndex > lastpc; resetIndex--){
                            if (sb.charAt(resetIndex)!=' ')
                                sb.insert(resetIndex, colorList.next());
                        }
                        sb.delete(firstpc-1, lastpc+1);
                    }
                }
            }
        }
        string = sb.toString();

        //remove all # if malformed, lazy fix
        TextComponent textComponent;
        try {
            textComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(string);
        } catch (IllegalStateException e){
            textComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(string.replaceAll("(?<=&)#(?![0-9a-fA-F])", ""));
        }

        return textComponent;
    }
    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
}
class EloitList {
    final LinkedList<String> list = new LinkedList<>();
    private int i = 0;
    private Random rand = new Random();
    private int mode = 0;
    public EloitList(){

    }
    public void add(String string ){
        list.add(string);
    }
    public boolean remove(String string ){
        return list.remove(string);
    }
    public boolean setMode(char c){
        if (c=='n')
            mode = 0;
        else if (c=='r')
            mode = 1;
        else
            return false;
        return true;
    }
    public String next(){
        if (mode == 0) {
            if (list.size() <= i)
                i = 0;
            int tmpi = i;
            i++;
            return list.get(tmpi);
        } else {
            int randInt = rand.nextInt(list.size());
            return list.get(randInt);
        }
    }

    @Override
    public String toString(){
        String c = "n";
        if (mode == 1)
            c = "r";
        return "{colours:"+list.toString()+", mode:"+c+", iteration:"+i+"}";
    }
}
