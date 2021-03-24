package org.elliotnash.teilochat.core.config;

public class PlayerFormat {
    public String msgprefix;
    public String name;

    @Override
    public String toString(){
       return msgprefix+"\n"+name;
    }

}
