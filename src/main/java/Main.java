import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import request.AnswerSender;
import request.RequestAcceptor;
import utility.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.*;

public class Main {
    private static final CollectionManager collectionManager = new CollectionManager();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileWorker fileWorker = new FileWorker(collectionManager);
            fileWorker.getToXmlFormat(fileWorker.getFilePath());
        }));
    }

    public static void main(String[] args) {
        DatagramSocket datagramSocket;
        CollectionManager collectionManager = Main.collectionManager;
        FileWorker fileWorker = new FileWorker(collectionManager);
        int port = 9898;
        try {
            if (args.length > 1) {
                collectionManager.setCollection(fileWorker.parse(args[0]));
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException exception) {
            System.out.println("Incorrect format of port.");
        } catch (IOException | ParserConfigurationException | SAXException exception) {
            System.out.println("File can't be read.");
        }
        if (port == 9898) {
            System.out.println("Port hasn't been identified. " + port + " will be used.");
        }
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Failed creating socket.");
            e.printStackTrace();
            return;
        }
        Logger logger = LoggerFactory.getLogger("Server");
        AnswerSender answerSender = new AnswerSender(logger);
        answerSender.setSocketAddress(datagramSocket.getLocalSocketAddress());
        WorkerFactory workerFactory = new WorkerFactory();
        Receiver receiver = new Receiver(collectionManager);
        Invoker invoker = new Invoker(receiver);
        invoker.initMap();
        RequestAcceptor requestAcceptor = new RequestAcceptor(workerFactory, logger, invoker, answerSender);
        requestAcceptor.acceptRequest(datagramSocket, datagramSocket.getLocalSocketAddress());
        try {
            logger.info("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
            System.out.println("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
    }
}
