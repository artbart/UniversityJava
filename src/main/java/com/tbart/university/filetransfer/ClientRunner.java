package com.tbart.university.filetransfer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tbart on 5/25/2014.
 */
public class ClientRunner {
    public static void main(String[] args) {
        handleStream(System.in);
    }

    public static void handleStream(InputStream is){
        MessageWorkerDef messageWorkerDef =  new MessageWorkerDef("D:/tmp", System.out);
        ClientNetworkManager networkManager = new ClientNetworkManager(9999, "localhost", "Vasya", messageWorkerDef);
        try (BufferedReader bufferRead = new BufferedReader(new InputStreamReader(is))) {
            while (true){
                String line = bufferRead.readLine();
                if (line.startsWith("shutdown")){
                    break;
                }

                if (line.startsWith("sent file")){
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
