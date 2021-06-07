package server;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class Listener extends Thread{
    private final DatagramSocket datagramSocket;
    private final ClientDataHandler handler;
    private final Logger logger;

    public Listener(DatagramSocket datagramSocket, ClientDataHandler handler, Logger logger){
        this.datagramSocket = datagramSocket;
        this.logger = logger;
        this.handler = handler;
    }

    public void read(){
        DatagramPacket request;
        while (!isInterrupted()){
            datagramSocket.disconnect();
            byte[] requestBuffer = new byte[1024];
            try {
                request = new DatagramPacket(requestBuffer, requestBuffer.length);
                datagramSocket.receive(request);
            } catch (SocketTimeoutException exception){
                continue;
            } catch (IOException exception) {
                exception.printStackTrace();
                continue;
            }
            logger.info("Request was received from ", request.getSocketAddress());
            handler.requestHandle(request);
        }
    }
}
