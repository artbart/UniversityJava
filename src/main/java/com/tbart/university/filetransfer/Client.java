package com.tbart.university.filetransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tbart on 5/25/2014.
 */
public class Client {
    public static void main(String[] args) {
        MessageWorkerDef messageWorkerDef = new MessageWorkerDef("D:/tmp", System.out);
        try (
                ClientNetworkManager networkManager = new ClientNetworkManager(Integer.parseInt(args[0]), "localhost", "Vasya", messageWorkerDef);
                ServerNetworkManager serverNetworkManager = new ServerNetworkManager(Integer.parseInt(args[1]), messageWorkerDef);
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in))
        ) {
            Thread server = new Thread(serverNetworkManager);
            server.start();
            while (true) {
                String line = bufferRead.readLine();
                if (line.startsWith("shutdown")) {
                    break;
                }

                if (line.startsWith("sent file")) {
                    networkManager.sentFile(line.substring(9).trim());
                } else {
                    networkManager.sentText(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
