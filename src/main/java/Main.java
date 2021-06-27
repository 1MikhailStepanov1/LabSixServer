import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import request.AnswerSender;
import request.RequestAcceptor;
import utility.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {


    public static void main(String[] args) {
        DatagramSocket datagramSocket;
        CollectionManager collectionManager = new CollectionManager();
        FileWorker fileWorker = new FileWorker(collectionManager);
        int port = 9898;
        try {
            if (args.length > 1) {
                fileWorker.setFilePath(args[0]);
                collectionManager.setCollection(fileWorker.parse(args[0]));
                port = Integer.parseInt(args[1]);
            } else if (args.length == 1) {
                fileWorker.setFilePath(args[0]);
                collectionManager.setCollection(fileWorker.parse(args[0]));
            } else {
                System.out.println("Incorrect command line arguments. Please, follow the format: \"file_path port\".");
                return;
            }
        } catch (NumberFormatException exception) {
            System.out.println("Incorrect format of port.");
        } catch (IOException | ParserConfigurationException | SAXException exception) {
            System.out.println("File can't be read.");
        }
        {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> fileWorker.getToXmlFormat(fileWorker.getFilePath())));
        }
        if (port == 9898) {
            System.out.println("Port hasn't been identified. " + port + " will be used.");
        }
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Failed creating socket. Socket is already used.");
            return;
        }
        Logger logger = LoggerFactory.getLogger("Server");
        AnswerSender answerSender = new AnswerSender(logger);
        System.out.println(datagramSocket.getLocalSocketAddress());
        answerSender.setSocketAddress(datagramSocket.getLocalSocketAddress());
        WorkerFactory workerFactory = new WorkerFactory();
        workerFactory.setStartId(collectionManager.getLastId());
        Receiver receiver = new Receiver(collectionManager, answerSender, workerFactory);
        Invoker invoker = new Invoker(receiver);
        invoker.initMap();
        RequestAcceptor requestAcceptor = new RequestAcceptor(workerFactory, logger, invoker, answerSender);
        requestAcceptor.acceptRequest(datagramSocket);
        try {
            logger.info("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
            System.out.println("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
    }
}

