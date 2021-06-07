package command.commandClasses;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

public  abstract class CommandAbstract implements Serializable {
    private static final long serialVersionUID = 32L;
    public abstract void exe(String arg, InetAddress address, int port) throws IOException;
}
