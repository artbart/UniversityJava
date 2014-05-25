package com.tbart.university.filetransfer;

/**
 * Created by tbart on 5/25/2014.
 */
public class ServerRunner {
    public static void main(String[] args) {
        MessageWorker messageWorker = new MessageWorker("D:/tmp", System.out);
        ServerNetworkManager serverNM = new ServerNetworkManager(9999, messageWorker);
        new Thread(serverNM).start();
    }
}
