package org.elliotnash.teilochat.core;

import java.util.List;

public interface PlatformUtils {
    boolean uniqueName(String name, Sender sender);
    Sender getSenderFromName(String name);
    List<Sender> getAllSenders();
}
