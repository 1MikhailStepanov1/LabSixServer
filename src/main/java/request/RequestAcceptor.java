package request;

import org.slf4j.Logger;
import utility.Invoker;
import utility.WorkerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RequestAcceptor {
    private final WorkerFactory workerFactory;
    private final Logger logger;
    private final Invoker invoker;
    private SerializationFromClient clientRequest = new SerializationFromClient("", "", null);
    private final AnswerSender answerSender;

    public RequestAcceptor(WorkerFactory workerFactory, Logger logger, Invoker invoker, AnswerSender answerSender) {
        this.workerFactory = workerFactory;
        this.logger = logger;
        this.invoker = invoker;
        this.answerSender = answerSender;

    }

    public void acceptRequest(DatagramSocket datagramSocket) {
        while (true) {
            byte[] acceptedRequest = new byte[2048];
            Object raw;
            String command;
            String arg;
            try {
                DatagramPacket datagramPacket = new DatagramPacket(acceptedRequest, acceptedRequest.length);
                datagramSocket.receive(datagramPacket);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                raw = objectInputStream.readObject();
                answerSender.setSocketAddress(datagramPacket.getSocketAddress());
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
                return;
            }
            if (raw instanceof SerializationFromClient) {
                clientRequest = (SerializationFromClient) raw;
                logger.info("Received command: " + clientRequest.getCommand() + " " + clientRequest.getArg());
            }
            if (clientRequest != null) {
                command = clientRequest.getCommand();
                arg = clientRequest.getArg();
                if (clientRequest.getWorker() != null) {
                    workerFactory.setLoadObject(clientRequest.getWorker());
                }
                invoker.exe(command, arg);
            }
        }
    }
}
