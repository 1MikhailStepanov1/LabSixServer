package server;

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
    private final DatagramSocket datagramSocket = null;
    private final LinkedList<SerializeData> answer;
    private final Logger logger;
    private SocketAddress socketAddress;

    public AnswerSender(Logger logger){
        this.logger = logger;
        answer = new LinkedList<>();
    }
    public void setSocketAddress(SocketAddress socketAddress){
        this.socketAddress = socketAddress;
    }

    public void addToAnswer(Object object){
        SerializeData temp = new SerializeData("Empty");
        if (object instanceof String){
            temp = new SerializeData((String) object);
        }
        if (object instanceof Worker){
            temp = new SerializeData((Worker) object);
        }
        answer.add(temp);
    }

    public void sendAnswer(){
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        if (answer.isEmpty()){
            return;
        }
        if (answer.peek().getData() instanceof Worker){
            answer.sort(Comparator.comparing((SerializeData worker) -> worker.getWorker().getName()));
        }
        try{
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            datagramSocket.connect(socketAddress);
            objectOutputStream.writeObject(answer);
            objectOutputStream.flush();
            logger.info("Answer length: " + answer.size());
            DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length);
            datagramSocket.send(datagramPacket);
            logger.info("Answer sent to " + datagramSocket.getRemoteSocketAddress());
        } catch (IOException exception) {
            logger.info("Failed sending answer " + exception.getMessage() + exception.getCause());
        }
        answer.clear();
    }
}
