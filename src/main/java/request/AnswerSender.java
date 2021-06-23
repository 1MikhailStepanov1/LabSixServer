package request;

import data.Worker;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.LinkedList;

public class AnswerSender {
    private final Logger logger;
    private SerializationForClient answer = new SerializationForClient(false, null, null,null);
    private SocketAddress socketAddress;
    private final DatagramSocket datagramSocket = null;

    public AnswerSender(Logger logger) {
        this.logger = logger;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void addToAnswer(boolean status, String message, Integer count, LinkedList<Worker> workers) {
        answer.setStatus(status);
        answer.setMessage(message);
        answer.setCount(count);
        answer.setWorkers(workers);
    }


    public void sendAnswer(){
        if (answer == null){
            return;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            datagramSocket.connect(socketAddress);
            objectOutputStream.writeObject(answer);
            objectOutputStream.flush();
            DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length);
            datagramSocket.send(datagramPacket);
            logger.info("Answer has been sent to " + datagramSocket.getRemoteSocketAddress());
        } catch (IOException exception) {
            logger.info("Failed sending answer." + exception.getMessage() + exception.getCause());
        }
        answer = null;
    }
}
