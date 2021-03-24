package org.elliotnash.teilochat.core;

public interface PlatformUtils {
    boolean uniqueName(String name, Sender sender);
    Sender getSenderFromName(String name);
}
