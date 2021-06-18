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
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileWorker fileWorker = new FileWorker();
            fileWorker.getToXmlFormat(fileWorker.getFilePath());
        }));
    }

    public static void main(String[] args) {
        DatagramSocket datagramSocket;
        FileWorker fileWorker = new FileWorker();
        int port = 9898;
        try {
            if (args.length > 1) {
                fileWorker.parse(args[0]);
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
        Invoker invoker = new Invoker();
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
