package command.commandClasses;

import command.CommandReceiver;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Save extends CommandAbstract{

    @Override
    public void exe(String arg, InetAddress address, int port) throws IOException {
        CommandReceiver commandReceiver = new CommandReceiver(address, port);
        commandReceiver.save();
    }
}
