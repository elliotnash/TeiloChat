package org.elliotnash.teilochat.core.config;

public class PlayerFormat {

    public String msgprefix;
    public String name;

    public PlayerFormat(){}
    public PlayerFormat(String msgprefix, String name){
        this.msgprefix = msgprefix;
        this.name = name;
    }
    @Override
    public String toString(){
       return "msgprefix: \""+msgprefix+"\", name: \""+name+"\"";
    }

}
