package command.commandClasses;

import command.CommandReceiver;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GroupCountingByPosition extends CommandAbstract {
    private static final long serialVersionUID = 32L;

    @Override
    public void exe(String arg, InetAddress address, int port) throws IOException {
        CommandReceiver commandReceiver = new CommandReceiver(address, port);
        commandReceiver.groupCountingByPosition();
    }
}
