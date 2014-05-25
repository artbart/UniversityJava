package com.tbart.university.filetransfer.oneserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tbart on 5/25/2014.
 */
public class ServerNetworkManager implements Runnable, Closeable {
    private Logger logger = LogManager.getFormatterLogger(ServerNetworkManager.class);

    private int serverPort;
    private ServerSocket serverSocket;

    private List<SocketWrapper> connections;


    public ServerNetworkManager(int serverPort) {
        this.serverPort = serverPort;
        connections =  Collections.synchronizedList(new ArrayList<SocketWrapper>());
    }

    @Override
    public void run() {
        MessageResender messageResender = new MessageResender(connections);
        long id = 0;
        Thread aliveChecker = new Thread(new AliveChecker(connections));
        aliveChecker.start();
        try {
            serverSocket = new ServerSocket(serverPort);
            logger.info("waiting for connections [port : %d]", serverPort);
            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Connection from: %s", socket.getInetAddress().toString());
                SocketWrapper socketWrapper = new SocketWrapper(socket, id++);
                connections.add(socketWrapper);
                new Thread(new ConnectionHandler(socketWrapper, System.out, messageResender)).start();
            }
        } catch (IOException e) {
            logger.info("Connection closed", e);
        }
        aliveChecker.interrupt();
    }

      @Override
    public void close() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

}
