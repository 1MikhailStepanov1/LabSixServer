import ch.qos.logback.core.rolling.RollingFileAppender;
import command.CommandReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import server.*;
import server.utils.CollectionManager;
import server.utils.CollectionValidator;
import server.utils.FileWorker;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    private static CommandReceiver commandReceiver;
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                commandReceiver.save();
            }
        });
    }

    public static void main(String[] args){
        Logger logger = LoggerFactory.getLogger(RollingFileAppender.class);
        CollectionManager collectionManager = new CollectionManager();
        WorkerFactory workerFactory = new WorkerFactory(1L);
        Scanner scanner = new Scanner(System.in);
        Console console = new Console(scanner);
        DatagramSocket datagramSocket;
        int port = 9898;
        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            } else if (args.length != 0){
                port = Integer.parseInt(args[0]);
            }
        } catch (NumberFormatException exception){
            System.out.println("Incorrect format.");
        }
        if (port == 9898){
            System.out.println("Port is not identified, " + port + " will be used.");
        }
        try {
            datagramSocket = new DatagramSocket(port);
            datagramSocket.setSoTimeout(60);
        } catch (SocketException e) {
            System.out.println("Failed creating socket.");
            e.printStackTrace();
            return;
        }
        AnswerSender answerSender = new AnswerSender(logger);
        CommandReceiver commandReceiver = new CommandReceiver(datagramSocket.getInetAddress(), port);
        ClientDataHandler handler = new ClientDataHandler(answerSender,workerFactory, logger);
        Listener listener = new Listener(datagramSocket, handler, logger);
        CollectionValidator validator = new CollectionValidator(collectionManager);
        FileWorker fileWorker = new FileWorker(collectionManager, validator);
        if (args.length != 0){
            try {
                fileWorker.setFilePath(args[0]);
                collectionManager.setCollection(fileWorker.parse());
            } catch (IOException | ParserConfigurationException | SAXException exception) {
                System.out.println("Something went wrong. PLease, try again.");
            }
        }
        listener.read();
        try{
            logger.info("Server started on address "+ InetAddress.getLocalHost() + " port: " + port);
            System.out.println("Server started on address "+ InetAddress.getLocalHost() + " port: " + port);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
        while (scanner.hasNext()){
            String line = console.readln();
            if (line.equals("exit")){
                break;
            }
            if (line.equals("save")){
                commandReceiver.save();
            }
        }
        listener.interrupt();

    }

}
