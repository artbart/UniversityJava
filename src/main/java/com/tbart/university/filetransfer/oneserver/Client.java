package com.tbart.university.filetransfer.oneserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by tbart on 5/26/2014.
 */
public class Client {
    private static Logger logger = LogManager.getFormatterLogger(Client.class);

    public static void main(String[] args) throws IOException {
        try (
                ClientNetworkManager clientNetworkManager = new ClientNetworkManager(9999, "localhost");
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in))
        ) {
            new Thread(clientNetworkManager).start();
            while (true) {
                String line = bufferRead.readLine();
                if (line.startsWith("shutdown")) {
                    break;
                }
                clientNetworkManager.sendMessage(line, "Vasya");
            }
        }
    }
}
