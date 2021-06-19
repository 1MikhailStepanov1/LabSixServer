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
    private LinkedList<Serialization> answerLine = new LinkedList<>();
    private LinkedList<Serialization> answerWorker = new LinkedList<>();
    private SocketAddress socketAddress;
    private final DatagramSocket datagramSocket = null;

    public AnswerSender(Logger logger) {
        this.logger = logger;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void addToAnswer(String line) {
        if (!line.equals("")){
            answerLine.add(new Serialization(line));
        }
    }
    public void addToAnswer(Worker worker) {
        if (worker != null){
            answerWorker.add(new Serialization(worker));
        }
    }

    public void sendAnswerWorkers() {
        if (answerWorker.isEmpty()) {
            return;
        }
        sending(answerWorker);
        answerWorker.clear();
    }
    public void sendAnswerLine() {
        if (answerLine.isEmpty()) {
            return;
        }
        sending(answerLine);
        answerLine.clear();
    }
    private void sending(LinkedList<Serialization> answer){
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
    }
}
