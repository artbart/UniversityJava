package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tbart on 5/25/2014.
 */
public class ServerNetworkManager  implements Runnable{
    private Logger logger = LogManager.getFormatterLogger(ServerNetworkManager.class);

    private int serverPort;
    private MessageWorker messageWorker;

    public ServerNetworkManager(int serverPort, MessageWorker messageWorker) {
        this.serverPort = serverPort;
        this.messageWorker = messageWorker;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)){
            logger.info("waiting for connections [port : %d]", serverPort);
            while (true) {
                try (Socket socket = serverSocket.accept()){
                    receiveConnection(socket);
                }
            }
        } catch (IOException e) {
            logger.error("Socket exception", e);
        }
    }

    private void receiveConnection(Socket socket) {
        logger.info("Connection from: %s", socket.getInetAddress().toString() );
        try (InputStream is = socket.getInputStream()){
            messageWorker.readMessage(is);
        } catch (IOException e) {
            logger.error("Cannot read client data", e);
        }
    }
}
