package request;

import data.Worker;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Comparator;
import java.util.LinkedList;

public class AnswerSender {
    private final Logger logger;
    private LinkedList<Serialization> answer = new LinkedList<>();
    private SocketAddress socketAddress;
    private final DatagramSocket datagramSocket = null;

    public AnswerSender(Logger logger) {
        this.logger = logger;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void addToAnswer(Object object) {
        Serialization temp = new Serialization("Empty");
        if (object instanceof String) {
            temp = new Serialization((String) object);
        }
        if (object instanceof Worker) {
            temp = new Serialization((Worker) object);
        }
        answer.add(temp);
    }

    public void sendAnswer() {
        if (answer.isEmpty()) {
            return;
        }
        if (answer.peek().getData() instanceof Worker) {
            answer.sort(Comparator.comparing((Serialization worker) -> worker.getWorker().getName()));
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
        answer.clear();
    }
}
