package server;

import command.SerializeCommand;
import command.commandClasses.CommandAbstract;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ClientDataHandler {
    private final AnswerSender answerSender;
    private final WorkerFactory workerFactory;
    private final Logger logger;

    public ClientDataHandler(AnswerSender answerSender, WorkerFactory workerFactory, Logger logger){
        this.answerSender = answerSender;
        this.workerFactory = workerFactory;
        this.logger = logger;
    }

    public void requestHandle(DatagramPacket packet){
        Object raw;
        ObjectInputStream objectInputStream;
        CommandAbstract commandAbstract = null;
        String arg = null;
        SerializeCommand serializeCommand = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        try{
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            raw = objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return;
        }
        if (raw instanceof SerializeCommand){
            serializeCommand = (SerializeCommand) raw;
            logger.info("Received command: "+serializeCommand.toString());
            answerSender.setSocketAddress(packet.getSocketAddress());
        }
        if (serializeCommand != null){
            commandAbstract = serializeCommand.getCommandAbstract();
            arg = serializeCommand.getArg();
            if (serializeCommand.getObject() != null){
                workerFactory.setLoadObject(serializeCommand.getObject());
            }
            try {
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                commandAbstract.exe(arg, address, port  );
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
