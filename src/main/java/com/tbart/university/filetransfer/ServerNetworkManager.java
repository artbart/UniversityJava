package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tbart on 5/25/2014.
 * <p/>
 * Server side of network manager.
 * <p/>
 * Receive connection from client side and extract messages using MessageWorker
 */
public class ServerNetworkManager implements Runnable, Closeable {
    private Logger logger = LogManager.getFormatterLogger(ServerNetworkManager.class);

    private int serverPort;
    private MessageWorker messageWorker;
    private ServerSocket serverSocket;

    public ServerNetworkManager(int serverPort, MessageWorker messageWorker) {
        this.serverPort = serverPort;
        this.messageWorker = messageWorker;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            logger.info("waiting for connections [port : %d]", serverPort);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    receiveConnection(socket);
                }
            }
        } catch (IOException e) {
            logger.info("Connection closed", e);
        }
    }

    /**
     * Handle connection from a client
     *
     * @param socket - socket, which is used to receive a message
     * @return -
     */
    private boolean receiveConnection(Socket socket) {
        logger.info("Connection from: %s", socket.getInetAddress().toString());
        boolean res = false;
        try (InputStream is = socket.getInputStream()) {
            res = messageWorker.readMessage(is);
        } catch (IOException e) {
            logger.error("Cannot read client data", e);
        }
        return res;
    }

    @Override
    public void close() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
