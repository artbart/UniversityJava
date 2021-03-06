package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by tbart on 5/25/2014.
 * <p/>
 * Client part of network manager.
 * <p/>
 * Sends messages and files to server part using MessageWorker
 */
public class ClientNetworkManager implements Closeable {
    private Logger logger = LogManager.getFormatterLogger(ClientNetworkManager.class);

    private int serverPort;
    private String serverIp;
    private String userName;
    private MessageWorker messageWorker;

    public ClientNetworkManager(int serverPort, String serverIp, String userName, MessageWorker messageWorker) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.userName = userName;
        this.messageWorker = messageWorker;
    }

    /**
     * Sends a text message to server using a socket
     *
     * @param text - text to sent
     */
    public void sentText(String text) {
        logger.info("sent text [text : %s]", text);

        try (Socket socket = new Socket(serverIp, serverPort); OutputStream os = socket.getOutputStream()) {
            messageWorker.sentText(os, text, userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message with file to server using a socket
     *
     * @param filePath - path to file which need to be sent
     */
    public void sentFile(String filePath) {
        logger.info("sent file [filePath : %s]", filePath);

        try (Socket socket = new Socket(serverIp, serverPort); OutputStream os = socket.getOutputStream()) {
            messageWorker.sentFile(os, filePath, userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {

    }
}
